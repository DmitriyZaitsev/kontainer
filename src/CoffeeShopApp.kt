class CoffeeShop : Injektor {
    override val kontainer = kontainer {
        provide<Heater> { ElectricHeater() }
        provide<Pump> { Thermosiphon(get()) }
        provide { CoffeeMaker(get(), get()) }
    }

    private val coffeeMaker: CoffeeMaker by injekt()

    fun makeCoffee() {
        coffeeMaker.brew()
    }
}

fun main() {
    CoffeeShop().makeCoffee()
}