package io.github.seorin21.farm.crops

import io.github.monun.tap.config.Config
import io.github.monun.tap.config.RangeDouble
import io.github.monun.tap.config.RangeInt

@Suppress("unused")
open class CropsConfig {
    @Config(required = true)
    val material: CropsType = CropsType.WHEAT

    @Config(required = true)
    val temperature: Double = 20.0

    @Config(required = true)
    @RangeDouble(min = 0.0)
    val tolerance: Double = 5.0 // 온도 범위 오차

    @Config(required = true)
    @RangeDouble(min = 0.0)
    val limit: Double = 5.0 // 극한:: 기준 온도 + 온도 범위 오차 + 극한

    @Config(required = true)
    @RangeInt(min = 1)
    val duration: Int = 60 * 60 * 24 * 30 // 전체 생장 기간

    @Config(required = true)
    @RangeDouble(min = 0.1)
    val yield: Double = 1.0 // 수확량
}