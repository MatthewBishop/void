package world.gregs.voidps.world.activity.dnd.shootingstar

import world.gregs.voidps.type.CoordGrid

enum class StarLocationData(val description: String, val tile: CoordGrid) {
    AL_KHARID_BANK("the north west edge of the Al Kharid Bank.", CoordGrid(3276, 3174)),
    AL_KHARID_MINE("the north of the scorpion mine.", CoordGrid(3299, 3308)),
    MAGE_TRAINING_ARENA("the south of the entrance to the Mage Training Arena.", CoordGrid(3348, 3284)),
    CRAFTING_GUILD("the Crafting Guild by the gold rocks.", CoordGrid(2940, 3281)),
    RIMMINGTON_MINING_SITE("the centre of the big mine north of Rimmington.", CoordGrid(2974, 3240)),
    FALADOR_WEST_MINE("the mine east of the dark wizard tower.", CoordGrid(2927, 3336)),
    SOUTHERN_CRANDOR_MINE("the beach beneath the south-western mining area on Crandor.", CoordGrid(2823, 3237)),
    KELDAGRIM_ENTRANCE("the mine located south of cave entrance to Keldagrim.", CoordGrid(2726, 3678)),
    JATIZO_MINE("the right of the mine's entrance on north-west Jatizso.", CoordGrid(2391, 3813)),
    LUNAR_ISLE_MINE("the ladder to the Rune essence mines on Lunar Isle.", CoordGrid(2146, 3942)),
    MISCELLANIA_MINE_SITE("south of coal mine on Miscellania.", CoordGrid(2531, 3887)),
    CENTRAL_FREMENNIK_MINING("the central Fremennik Isles mining site (Neitiznot Drakolith mine).", CoordGrid(2375, 3833)),
    RELLEKKA_MINING_SITE("the mining site in the fenced off area inside the town of Rellekka. ", CoordGrid(2676, 3698)),
    ARDOUGNE_MINING_SITE("the iron rocks north of the monastery, south of East Ardougne.", CoordGrid(2701, 3333)),
    COAL_TRUCK_MINING("the middle of the coal rocks that supply the Coal trucks.", CoordGrid(2586, 3477)),
    FIGHT_ARENA_MINING("the mining spot north-east of Yanille, south-west of Port Khazard.", CoordGrid(2634, 3133))
}