//<editor-fold desc="Preconditions">
interface Heater {
    val isHot: Boolean
    fun on()
    fun off()
}

class ElectricHeater : Heater {
    override var isHot: Boolean = false

    override fun on() {
        println("~ ~ ~ heating ~ ~ ~")
        isHot = true
    }

    override fun off() {
        isHot = false
    }
}

interface Pump {
    fun pump()
}

class Thermosiphon(private val heater: Heater) : Pump {
    override fun pump() {
        if (heater.isHot) {
            println("=> => pumping => =>")
        }
    }
}

class CoffeeMaker(
    private val heater: Heater,
    private val pump: Pump
) {
    fun brew() {
        heater.on()
        pump.pump()
        println("[_]P coffee! [_]P")
    }
}
//</editor-fold>