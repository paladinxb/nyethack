fun main(args: Array<String>) {
    var healthPoints = 100
    var isBlessed = true
    val isImmortal = false
    // Aura

    val healthStatus = formatHealthStatus(healthPoints, isBlessed)
    Game.play()


}
private fun formatHealthStatus(healthPoints: Int, isBlessed: Boolean) =
        when (healthPoints) {
            100 -> "is in excellent condition!"
            in 90..99 -> "has a few scratches."
            in 75..89 -> if (isBlessed) {
                "has some minor wounds, but is healing quite quickly!"
            } else {
                "has some minor wounds." }
            in 15..74 -> "looks pretty hurt."
            else -> "is in awful condition!" }




object Game {
    private val player = Player("Madrigal")
    private var currentRoom: Room = TownSquare()

    private var worldMap = listOf(
            listOf(currentRoom, Room("Tavern"), Room("Back Room")),
            listOf(Room("Long Corridor"), Room("Generic Room")))

    private fun move(directionInput: String) =
            try {
                val direction = Direction.valueOf(directionInput.toUpperCase())
                val newPosition = direction.updateCoordinate(player.currentPosition)
                if (!newPosition.isInBounds) {
                    throw IllegalStateException("$direction is out of bounds.")
                }

                val newRoom = worldMap[newPosition.y][newPosition.x]
                player.currentPosition = newPosition
                currentRoom = newRoom
                "OK, you move $direction to the ${newRoom.name}.\n${newRoom.load()}"
            } catch (e: Exception) {
                "Invalid direction: $directionInput."
            }


    init {
        println("Welcome, adventurer.")
        player.castFireBall()
    }

    fun play() {
        while (true) {
            println(currentRoom.description())
            println(currentRoom.load())
            //Player status
            printPlayerStatus(player)
            print("> Enter your command: ")
            println(GameInput(readLine()).processCommand())
        }
    }

    private fun printPlayerStatus(player: Player) {
        println("(Aura: ${player.auraColor()}) " +
                "(Blessed: ${if (player.isBlessed) "YES" else "No"})")
        println("${player.name} ${player.formatHealthStatus()}")
    }

    private class GameInput(arg: String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1, { "" })

        fun processCommand() = when (command.toLowerCase()) {
            "move" -> move(argument)
            else -> commandNotFound()
        }

        private fun commandNotFound() = "I'm not quite sure what you're trying to do"
    }
}


fun auraColor(isBlessed: Boolean,
                      healthPoints: Int,
                      isImmortal: Boolean): String {
    val auraVisible = isBlessed && healthPoints > 50 || isImmortal
    val auraColor = if (auraVisible) "GREEN" else "NONE"
    return auraColor
}
