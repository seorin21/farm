package io.github.seorin21.farm.event

import io.github.monun.tap.event.EventEntity
import io.github.monun.tap.event.EventEntityProvider
import io.github.monun.tap.event.HandlerStatement
import io.github.monun.tap.event.ListenerStatement
import io.github.monun.tap.event.RegisteredEntityListener
import com.google.common.collect.MapMaker
import io.github.monun.tap.event.EventTools.getRegistrationClass
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin

@Suppress("unused")
/**
 * 지정한 [Entity] 전용 이벤트를 처리하는 클래스
 * 이 클래스는 Thread-Unsafe 입니다.
 * Async된 [Event]는 지원이 불확실합니다
 */
class EntityEventManager @JvmOverloads constructor(
    private val plugin: Plugin,
    private val priority: EventPriority = EventPriority.NORMAL
) {
    private val statements = HashMap<Class<*>, ListenerStatement>()

    private val listeners = HashMap<Class<*>, EventListener>()

    private val entities = MapMaker().weakKeys().makeMap<Entity, EventEntity>()

    private val globalListeners = HashMap<Class<*>, EventListener>()
    private val entityTypeListeners = HashMap<EntityType, HashMap<Class<*>, EventListener>>()

    private val eventExecutor = EventExecutor { listener: Listener, event: Event ->
        (listener as EventListener).onEvent(event)
    }

    /**
     * 지정한 [Entity] 전용 이벤트 리스너를 등록합니다.
     *
     * @param entity 대상
     * @param listener 이벤트리스너
     */
    fun registerEvents(entity: Entity, listener: Listener): RegisteredEntityListener {
        require(entity.isValid || (entity is Player && entity.isOnline)) { "Invalid entity: $entity" }

        val listenerStatement = createRegisteredListenerStatement(listener.javaClass)
        val eventEntity = entities.computeIfAbsent(entity) { EventEntity() }

        val registeredEntityListener = RegisteredEntityListener(eventEntity, listenerStatement, listener)
        eventEntity.register(registeredEntityListener)

        return registeredEntityListener
    }

    private fun createRegisteredListenerStatement(listenerClass: Class<*>): ListenerStatement {
        return statements.computeIfAbsent(listenerClass) { clazz: Class<*> ->
            val statement = ListenerStatement.getOrCreate(clazz)

            for (statementStatement in statement.handlerStatements) {
                registerEvent(statementStatement)
            }

            statement
        }
    }

    private fun registerEvent(statement: HandlerStatement) {
        val registrationClass = statement.registrationClass

        val listener = listeners.computeIfAbsent(registrationClass) { clazz: Class<*> ->
            val newListener = EventListener()
            plugin.server.pluginManager.registerEvent(
                clazz.asSubclass(Event::class.java),
                newListener,
                priority,
                eventExecutor,
                plugin,
                false
            )

            newListener
        }

        listener.addProvider(statement.provider)
    }

    /**
     * 지정한 [Entity] 전용 이벤트 리스너를 제거합니다.
     *
     * @param entity 대상
     * @param listener 이벤트리스너
     */
    fun unregisterEvent(entity: Entity, listener: Listener) {
        val eventEntity = entities[entity]

        if (eventEntity != null) {
            val statement = statements[listener.javaClass]

            if (statement != null) {
                eventEntity.unregister(statement, listener)
            }
        }
    }

    /**
     * 모든 엔티티에 대한 이벤트 리스너를 등록합니다.
     *
     * @param listener 이벤트리스너
     */
    fun registerGlobalEvents(listener: Listener) {
        val listenerStatement = createRegisteredListenerStatement(listener.javaClass)

        for (statementStatement in listenerStatement.handlerStatements) {
            registerGlobalEvent(statementStatement, listener)
        }
    }

    /**
     * 특정 엔티티 종류에 대한 이벤트 리스너를 등록합니다.
     *
     * @param entityType 대상 엔티티 종류
     * @param listener 이벤트리스너
     */
    fun registerEntityTypeEvents(entityType: EntityType, listener: Listener) {
        val listenerStatement = createRegisteredListenerStatement(listener.javaClass)

        for (statementStatement in listenerStatement.handlerStatements) {
            registerEntityTypeEvent(entityType, statementStatement, listener)
        }
    }

    private fun registerGlobalEvent(statement: HandlerStatement, listener: Listener) {
        val registrationClass = statement.registrationClass

        val eventListener = globalListeners.computeIfAbsent(registrationClass) { clazz: Class<*> ->
            val newListener = EventListener()
            plugin.server.pluginManager.registerEvent(
                clazz.asSubclass(Event::class.java),
                newListener,
                priority,
                eventExecutor,
                plugin,
                false
            )
            newListener
        }

        eventListener.addHandler(statement, listener)
    }

    private fun registerEntityTypeEvent(entityType: EntityType, statement: HandlerStatement, listener: Listener) {
        val registrationClass = statement.registrationClass

        val entityTypeListenerMap = entityTypeListeners.computeIfAbsent(entityType) { HashMap() }
        val eventListener = entityTypeListenerMap.computeIfAbsent(registrationClass) { clazz: Class<*> ->
            val newListener = EventListener()
            plugin.server.pluginManager.registerEvent(
                clazz.asSubclass(Event::class.java),
                newListener,
                priority,
                eventExecutor,
                plugin,
                false
            )
            newListener
        }

        eventListener.addHandler(statement, listener)
    }

    /**
     * 글로벌 이벤트 리스너를 제거합니다.
     *
     * @param listener 이벤트리스너
     */
    fun unregisterGlobalEvents(listener: Listener) {
        for ((_, eventListener) in globalListeners) {
            eventListener.removeHandler(listener)
        }
    }

    /**
     * 특정 엔티티 종류에 대한 이벤트 리스너를 제거합니다.
     *
     * @param entityType 대상 엔티티 종류
     * @param listener 이벤트리스너
     */
    fun unregisterEntityTypeEvents(entityType: EntityType, listener: Listener) {
        val entityTypeListenerMap = entityTypeListeners[entityType]
        entityTypeListenerMap?.forEach { (_, eventListener) ->
            eventListener.removeHandler(listener)
        }
    }

    /**
     * 등록된 모든 리스너를 제거합니다.
     */
    fun unregisterAll() {
        for (eventEntity in entities.values) {
            eventEntity.unregisterAll()
        }

        for (listener in listeners.values) {
            HandlerList.unregisterAll(listener)
        }

        entities.clear()
        listeners.clear()
        statements.clear()

        for (listener in globalListeners.values) {
            HandlerList.unregisterAll(listener)
        }
        globalListeners.clear()

        for (entityTypeListenerMap in entityTypeListeners.values) {
            for (listener in entityTypeListenerMap.values) {
                HandlerList.unregisterAll(listener)
            }
        }
        entityTypeListeners.clear()
    }

    private inner class EventListener : Listener {
        private val providers = LinkedHashSet<EventEntityProvider>()
        private val handlers = LinkedHashSet<Pair<HandlerStatement, Listener>>()

        private var bake: Array<EventEntityProvider>? = null

        fun onEvent(event: Event) {
            for (provider in getBake()) {
                val eventClass = event.javaClass

                if (provider.eventClass.isAssignableFrom(eventClass)) {
                    val entity = provider.provider.getFrom(event)

                    if (entity != null) {
                        val eventEntity = entities[entity]

                        if (eventEntity != null) {
                            val regClass = getRegistrationClass(eventClass)
                            val handlers = eventEntity.getHandlerList(regClass)

                            handlers?.callEvent(event, provider)
                        }
                    }
                }
            }

            for ((statement, listener) in handlers) {
                val eventClass = event.javaClass

                if (statement.eventClass.isAssignableFrom(eventClass)) {
                    val entity = statement.provider.provider.getFrom(event)

                    if (entity != null) {
                        if (this@EventListener in globalListeners.values ||
                            (entity.type in entityTypeListeners && this@EventListener in entityTypeListeners[entity.type]!!.values)
                        ) {
                            statement.executor.execute(listener, event)
                        }
                    }
                }
            }
        }

        private fun getBake(): Array<EventEntityProvider> {
            val bake = bake
            return bake ?: providers.toTypedArray().also { this.bake = it }
        }

        fun addProvider(provider: EventEntityProvider) {
            providers.add(provider)
            bake = null
        }

        fun addHandler(statement: HandlerStatement, listener: Listener) {
            handlers.add(Pair(statement, listener))
        }

        fun removeHandler(listener: Listener) {
            handlers.removeIf { it.second == listener }
        }
    }
}
