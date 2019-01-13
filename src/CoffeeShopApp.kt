class CoffeeShop : Injektor {
    override val kontainer = kontainer {
        weak<Heater> { ElectricHeater() }
        singleton<Pump> { Thermosiphon(resolve()) }
        provider { CoffeeMaker(resolve(), resolve()) }
    }

    private val coffeeMaker: CoffeeMaker by injekt()

    fun makeCoffee() {
        coffeeMaker.brew()
    }
}

fun main() {
    CoffeeShop().makeCoffee()
}