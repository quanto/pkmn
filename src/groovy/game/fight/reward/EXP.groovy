package game.fight.reward

import game.fight.log.DingLog
import game.fight.log.EvolveLog
import game.fight.log.MessageLog
import game.context.FightPlayer
import game.context.Fight
import game.Evolution
import game.LearnableMove
import game.Moves
import game.OwnerPokemon
import game.fight.log.SwitchLog

class EXP {

    public static void giveEXP(Fight fight, FightPlayer ownfightPlayer, FightPlayer opponentfightPlayer, boolean isWild, int exp, boolean isCurrentOwnerPokemon)
    {

        ownfightPlayer.ownerPokemon.xp += exp

        // Kijk voor lvl up
        int newLevel = getLevel(ownfightPlayer.ownerPokemon.xp, ownfightPlayer.ownerPokemon.pokemon.levelRate)
        if (newLevel > ownfightPlayer.ownerPokemon.level)
        {

            int lvlDiff = newLevel - ownfightPlayer.ownerPokemon.level;
            for (int i=0;i<lvlDiff;i++)
            {
                int newLevelLoop = ownfightPlayer.ownerPokemon.level + i + 1

                fight.roundResult.battleActions.add(new MessageLog("${ownfightPlayer.ownerPokemon.pokemon.name} grew to level ${newLevelLoop}!"))

                if (isCurrentOwnerPokemon){
                    fight.roundResult.battleActions.add(new DingLog(newLevelLoop,ownfightPlayer.playerNr))
                }

                Evolution evolution = Evolution.findByFromPokemonAndLevel(ownfightPlayer.ownerPokemon.pokemon,newLevelLoop)
                // kijk of pokemon evalueert

                if (evolution)
                {

                    fight.roundResult.battleActions.add(new MessageLog(ownfightPlayer.ownerPokemon.pokemon.name + " evolved into " + evolution.toPokemon.name + "."))

                    ownfightPlayer.ownerPokemon.pokemon = evolution.toPokemon

                    if (isCurrentOwnerPokemon){
                        fight.roundResult.battleActions.add(new SwitchLog(ownfightPlayer.ownerPokemon, ownfightPlayer.playerNr))
                    }
                }

                // Kijk of er move geleerd kan worden
                List<LearnableMove> learnableMoveList = LearnableMove.findAllByPokemonAndLearnLevel(ownfightPlayer.ownerPokemon.pokemon,newLevelLoop)

                learnableMoveList.each { LearnableMove learnableMove ->
                    if (learnableMove.move.implemented){
                        Moves.learnMove(fight,ownfightPlayer,learnableMove.move)
                    }
                }

            }
            // zet nieuwe level
            ownfightPlayer.ownerPokemon.level = newLevel
            ownfightPlayer.level = newLevel

        }
        ownfightPlayer.ownerPokemon.save(flush: true)
    }

    public static void distributeExp(Fight fight, FightPlayer ownfightPlayer, FightPlayer opponentfightPlayer, boolean isWild){

        int exp = calcXP(opponentfightPlayer.ownerPokemon.pokemon.baseEXP,opponentfightPlayer.level,isWild)

        fight.usedPokemon.each { OwnerPokemon ownerPokemon ->
            int expShare = Math.floor(exp / fight.usedPokemon.size())
            fight.roundResult.battleActions.add(new MessageLog(ownerPokemon.pokemon.name + " gains ${expShare} exp."))

            boolean isCurrentOwnerPokemon = ownerPokemon.id == fight.fightPlayer1.ownerPokemon.id
            giveEXP(fight, ownfightPlayer, opponentfightPlayer, isWild, exp, isCurrentOwnerPokemon)
        }
        // Leave only the current ownerPokemon in the used list
        fight.usedPokemon = [fight.usedPokemon.find{ it.id == fight.fightPlayer1.ownerPokemon.id}]
    }

    public static int calcXP(int baseXp, int level, boolean isWild)
    {
        int trainer = 1; // altijd 1, normaal gesproken factor hoger wanneer je niet dehuidige trainer bent
        // trainer 1 || 1.5 wanneer je niet de huidige eigenaar bent, 1.5
        // wild 1 || 1.5 wanneer je geen wilde pokemon vecht 1.5
        double wild
        if (isWild)
            wild = 1;
        else
            wild = 1.5;

        int xp = Math.floor(((baseXp * level) * trainer * wild) / 7);

        return xp;
    }

    public static int getExpPercentage(int level, String levelRate, int exp)
    {
        int xpPrev = getExp(level,levelRate)
        int xpNext = getExp(level + 1,levelRate)

        return Math.round(100 / (xpNext - xpPrev) * (exp - xpPrev));
    }


    public static int getExp(int lvl, String levelRate)
    {
        int xp
        if (levelRate == "Erratic")
        {
            if(lvl == 1){ xp = 0; }
            else if(lvl == 2){ xp = 4; }
            else if(lvl == 3){ xp = 16; }
            else if(lvl == 4){ xp = 38; }
            else if(lvl == 5){ xp = 75; }
            else if(lvl == 6){ xp = 129; }
            else if(lvl == 7){ xp = 205; }
            else if(lvl == 8){ xp = 307; }
            else if(lvl == 9){ xp = 437; }
            else if(lvl == 10){ xp = 600; }
            else if(lvl == 11){ xp = 798; }
            else if(lvl == 12){ xp = 1036; }
            else if(lvl == 13){ xp = 1318; }
            else if(lvl == 14){ xp = 1646; }
            else if(lvl == 15){ xp = 2025; }
            else if(lvl == 16){ xp = 2457; }
            else if(lvl == 17){ xp = 2947; }
            else if(lvl == 18){ xp = 3499; }
            else if(lvl == 19){ xp = 4115; }
            else if(lvl == 20){ xp = 4800; }
            else if(lvl == 21){ xp = 5556; }
            else if(lvl == 22){ xp = 6388; }
            else if(lvl == 23){ xp = 7300; }
            else if(lvl == 24){ xp = 8294; }
            else if(lvl == 25){ xp = 9375; }
            else if(lvl == 26){ xp = 10545; }
            else if(lvl == 27){ xp = 11809; }
            else if(lvl == 28){ xp = 13171; }
            else if(lvl == 29){ xp = 14633; }
            else if(lvl == 30){ xp = 16200; }
            else if(lvl == 31){ xp = 17874; }
            else if(lvl == 32){ xp = 19660; }
            else if(lvl == 33){ xp = 21562; }
            else if(lvl == 34){ xp = 23582; }
            else if(lvl == 35){ xp = 25725; }
            else if(lvl == 36){ xp = 27993; }
            else if(lvl == 37){ xp = 30391; }
            else if(lvl == 38){ xp = 32923; }
            else if(lvl == 39){ xp = 35591; }
            else if(lvl == 40){ xp = 38400; }
            else if(lvl == 41){ xp = 41352; }
            else if(lvl == 42){ xp = 44452; }
            else if(lvl == 43){ xp = 47704; }
            else if(lvl == 44){ xp = 51110; }
            else if(lvl == 45){ xp = 54675; }
            else if(lvl == 46){ xp = 58401; }
            else if(lvl == 47){ xp = 62293; }
            else if(lvl == 48){ xp = 66355; }
            else if(lvl == 49){ xp = 70589; }
            else if(lvl == 50){ xp = 75000; }
            else if(lvl == 51){ xp = 79590; }
            else if(lvl == 52){ xp = 84364; }
            else if(lvl == 53){ xp = 89326; }
            else if(lvl == 54){ xp = 94478; }
            else if(lvl == 55){ xp = 99825; }
            else if(lvl == 56){ xp = 105369; }
            else if(lvl == 57){ xp = 111115; }
            else if(lvl == 58){ xp = 117067; }
            else if(lvl == 59){ xp = 123227; }
            else if(lvl == 60){ xp = 129600; }
            else if(lvl == 61){ xp = 136188; }
            else if(lvl == 62){ xp = 142996; }
            else if(lvl == 63){ xp = 150028; }
            else if(lvl == 64){ xp = 157286; }
            else if(lvl == 65){ xp = 164775; }
            else if(lvl == 66){ xp = 172497; }
            else if(lvl == 67){ xp = 180457; }
            else if(lvl == 68){ xp = 188659; }
            else if(lvl == 69){ xp = 197105; }
            else if(lvl == 70){ xp = 205800; }
            else if(lvl == 71){ xp = 214746; }
            else if(lvl == 72){ xp = 223948; }
            else if(lvl == 73){ xp = 233410; }
            else if(lvl == 74){ xp = 243134; }
            else if(lvl == 75){ xp = 253125; }
            else if(lvl == 76){ xp = 263385; }
            else if(lvl == 77){ xp = 273919; }
            else if(lvl == 78){ xp = 284731; }
            else if(lvl == 79){ xp = 295823; }
            else if(lvl == 80){ xp = 307200; }
            else if(lvl == 81){ xp = 318864; }
            else if(lvl == 82){ xp = 330820; }
            else if(lvl == 83){ xp = 343072; }
            else if(lvl == 84){ xp = 355622; }
            else if(lvl == 85){ xp = 368475; }
            else if(lvl == 86){ xp = 381633; }
            else if(lvl == 87){ xp = 395101; }
            else if(lvl == 88){ xp = 408883; }
            else if(lvl == 89){ xp = 422981; }
            else if(lvl == 90){ xp = 437400; }
            else if(lvl == 91){ xp = 452142; }
            else if(lvl == 92){ xp = 467212; }
            else if(lvl == 93){ xp = 482614; }
            else if(lvl == 94){ xp = 498350; }
            else if(lvl == 95){ xp = 514425; }
            else if(lvl == 96){ xp = 530841; }
            else if(lvl == 97){ xp = 547603; }
            else if(lvl == 98){ xp = 564715; }
            else if(lvl == 99){ xp = 582179; }
            else if(lvl == 100){ xp = 600000; }
        }
        else if (levelRate == "Fast")
        {
            if(lvl == 1){ xp = 0; }
            else if(lvl == 2){ xp = 6; }
            else if(lvl == 3){ xp = 21; }
            else if(lvl == 4){ xp = 51; }
            else if(lvl == 5){ xp = 100; }
            else if(lvl == 6){ xp = 172; }
            else if(lvl == 7){ xp = 274; }
            else if(lvl == 8){ xp = 409; }
            else if(lvl == 9){ xp = 583; }
            else if(lvl == 10){ xp = 800; }
            else if(lvl == 11){ xp = 1064; }
            else if(lvl == 12){ xp = 1382; }
            else if(lvl == 13){ xp = 1757; }
            else if(lvl == 14){ xp = 2195; }
            else if(lvl == 15){ xp = 2700; }
            else if(lvl == 16){ xp = 3276; }
            else if(lvl == 17){ xp = 3930; }
            else if(lvl == 18){ xp = 4665; }
            else if(lvl == 19){ xp = 5487; }
            else if(lvl == 20){ xp = 6400; }
            else if(lvl == 21){ xp = 7408; }
            else if(lvl == 22){ xp = 8518; }
            else if(lvl == 23){ xp = 9733; }
            else if(lvl == 24){ xp = 11059; }
            else if(lvl == 25){ xp = 12500; }
            else if(lvl == 26){ xp = 14060; }
            else if(lvl == 27){ xp = 15746; }
            else if(lvl == 28){ xp = 17561; }
            else if(lvl == 29){ xp = 19511; }
            else if(lvl == 30){ xp = 21600; }
            else if(lvl == 31){ xp = 23832; }
            else if(lvl == 32){ xp = 26214; }
            else if(lvl == 33){ xp = 28749; }
            else if(lvl == 34){ xp = 31443; }
            else if(lvl == 35){ xp = 34300; }
            else if(lvl == 36){ xp = 37324; }
            else if(lvl == 37){ xp = 40522; }
            else if(lvl == 38){ xp = 43897; }
            else if(lvl == 39){ xp = 47455; }
            else if(lvl == 40){ xp = 51200; }
            else if(lvl == 41){ xp = 55136; }
            else if(lvl == 42){ xp = 59270; }
            else if(lvl == 43){ xp = 63605; }
            else if(lvl == 44){ xp = 68147; }
            else if(lvl == 45){ xp = 72900; }
            else if(lvl == 46){ xp = 77868; }
            else if(lvl == 47){ xp = 83058; }
            else if(lvl == 48){ xp = 88473; }
            else if(lvl == 49){ xp = 94119; }
            else if(lvl == 50){ xp = 100000; }
            else if(lvl == 51){ xp = 106120; }
            else if(lvl == 52){ xp = 112486; }
            else if(lvl == 53){ xp = 119101; }
            else if(lvl == 54){ xp = 125971; }
            else if(lvl == 55){ xp = 133100; }
            else if(lvl == 56){ xp = 140492; }
            else if(lvl == 57){ xp = 148154; }
            else if(lvl == 58){ xp = 156089; }
            else if(lvl == 59){ xp = 164303; }
            else if(lvl == 60){ xp = 172800; }
            else if(lvl == 61){ xp = 181584; }
            else if(lvl == 62){ xp = 190662; }
            else if(lvl == 63){ xp = 200037; }
            else if(lvl == 64){ xp = 209715; }
            else if(lvl == 65){ xp = 219700; }
            else if(lvl == 66){ xp = 229996; }
            else if(lvl == 67){ xp = 240610; }
            else if(lvl == 68){ xp = 251545; }
            else if(lvl == 69){ xp = 262807; }
            else if(lvl == 70){ xp = 274400; }
            else if(lvl == 71){ xp = 286328; }
            else if(lvl == 72){ xp = 298598; }
            else if(lvl == 73){ xp = 311213; }
            else if(lvl == 74){ xp = 324179; }
            else if(lvl == 75){ xp = 337500; }
            else if(lvl == 76){ xp = 351180; }
            else if(lvl == 77){ xp = 365226; }
            else if(lvl == 78){ xp = 379641; }
            else if(lvl == 79){ xp = 394431; }
            else if(lvl == 80){ xp = 409600; }
            else if(lvl == 81){ xp = 425152; }
            else if(lvl == 82){ xp = 441094; }
            else if(lvl == 83){ xp = 457429; }
            else if(lvl == 84){ xp = 474163; }
            else if(lvl == 85){ xp = 491300; }
            else if(lvl == 86){ xp = 508844; }
            else if(lvl == 87){ xp = 526802; }
            else if(lvl == 88){ xp = 545177; }
            else if(lvl == 89){ xp = 563975; }
            else if(lvl == 90){ xp = 583200; }
            else if(lvl == 91){ xp = 602856; }
            else if(lvl == 92){ xp = 622950; }
            else if(lvl == 93){ xp = 643485; }
            else if(lvl == 94){ xp = 664467; }
            else if(lvl == 95){ xp = 685900; }
            else if(lvl == 96){ xp = 707788; }
            else if(lvl == 97){ xp = 730138; }
            else if(lvl == 98){ xp = 752953; }
            else if(lvl == 99){ xp = 776239; }
            else if(lvl == 100){ xp = 800000; }
        }
        else if (levelRate == "Medium-Fast")
        {
            if(lvl == 1){ xp = 0; }
            else if(lvl == 2){ xp = 8; }
            else if(lvl == 3){ xp = 27; }
            else if(lvl == 4){ xp = 64; }
            else if(lvl == 5){ xp = 125; }
            else if(lvl == 6){ xp = 216; }
            else if(lvl == 7){ xp = 343; }
            else if(lvl == 8){ xp = 512; }
            else if(lvl == 9){ xp = 729; }
            else if(lvl == 10){ xp = 1000; }
            else if(lvl == 11){ xp = 1331; }
            else if(lvl == 12){ xp = 1728; }
            else if(lvl == 13){ xp = 2197; }
            else if(lvl == 14){ xp = 2744; }
            else if(lvl == 15){ xp = 3375; }
            else if(lvl == 16){ xp = 4096; }
            else if(lvl == 17){ xp = 4913; }
            else if(lvl == 18){ xp = 5832; }
            else if(lvl == 19){ xp = 6859; }
            else if(lvl == 20){ xp = 8000; }
            else if(lvl == 21){ xp = 9261; }
            else if(lvl == 22){ xp = 10648; }
            else if(lvl == 23){ xp = 12167; }
            else if(lvl == 24){ xp = 13824; }
            else if(lvl == 25){ xp = 15625; }
            else if(lvl == 26){ xp = 17576; }
            else if(lvl == 27){ xp = 19683; }
            else if(lvl == 28){ xp = 21952; }
            else if(lvl == 29){ xp = 24389; }
            else if(lvl == 30){ xp = 27000; }
            else if(lvl == 31){ xp = 29791; }
            else if(lvl == 32){ xp = 32768; }
            else if(lvl == 33){ xp = 35937; }
            else if(lvl == 34){ xp = 39304; }
            else if(lvl == 35){ xp = 42875; }
            else if(lvl == 36){ xp = 46656; }
            else if(lvl == 37){ xp = 50653; }
            else if(lvl == 38){ xp = 54872; }
            else if(lvl == 39){ xp = 59319; }
            else if(lvl == 40){ xp = 64000; }
            else if(lvl == 41){ xp = 68921; }
            else if(lvl == 42){ xp = 74088; }
            else if(lvl == 43){ xp = 79507; }
            else if(lvl == 44){ xp = 85184; }
            else if(lvl == 45){ xp = 91125; }
            else if(lvl == 46){ xp = 97336; }
            else if(lvl == 47){ xp = 103823; }
            else if(lvl == 48){ xp = 110592; }
            else if(lvl == 49){ xp = 117649; }
            else if(lvl == 50){ xp = 125000; }
            else if(lvl == 51){ xp = 132651; }
            else if(lvl == 52){ xp = 140608; }
            else if(lvl == 53){ xp = 148877; }
            else if(lvl == 54){ xp = 157464; }
            else if(lvl == 55){ xp = 166375; }
            else if(lvl == 56){ xp = 175616; }
            else if(lvl == 57){ xp = 185193; }
            else if(lvl == 58){ xp = 195112; }
            else if(lvl == 59){ xp = 205379; }
            else if(lvl == 60){ xp = 216000; }
            else if(lvl == 61){ xp = 226981; }
            else if(lvl == 62){ xp = 238328; }
            else if(lvl == 63){ xp = 250047; }
            else if(lvl == 64){ xp = 262144; }
            else if(lvl == 65){ xp = 274625; }
            else if(lvl == 66){ xp = 287496; }
            else if(lvl == 67){ xp = 300763; }
            else if(lvl == 68){ xp = 314432; }
            else if(lvl == 69){ xp = 328509; }
            else if(lvl == 70){ xp = 343000; }
            else if(lvl == 71){ xp = 357911; }
            else if(lvl == 72){ xp = 373248; }
            else if(lvl == 73){ xp = 389017; }
            else if(lvl == 74){ xp = 405224; }
            else if(lvl == 75){ xp = 421875; }
            else if(lvl == 76){ xp = 438976; }
            else if(lvl == 77){ xp = 456533; }
            else if(lvl == 78){ xp = 474552; }
            else if(lvl == 79){ xp = 493039; }
            else if(lvl == 80){ xp = 512000; }
            else if(lvl == 81){ xp = 531441; }
            else if(lvl == 82){ xp = 551368; }
            else if(lvl == 83){ xp = 571787; }
            else if(lvl == 84){ xp = 592704; }
            else if(lvl == 85){ xp = 614125; }
            else if(lvl == 86){ xp = 636056; }
            else if(lvl == 87){ xp = 658503; }
            else if(lvl == 88){ xp = 681472; }
            else if(lvl == 89){ xp = 704969; }
            else if(lvl == 90){ xp = 729000; }
            else if(lvl == 91){ xp = 753571; }
            else if(lvl == 92){ xp = 778688; }
            else if(lvl == 93){ xp = 804357; }
            else if(lvl == 94){ xp = 830584; }
            else if(lvl == 95){ xp = 857375; }
            else if(lvl == 96){ xp = 884736; }
            else if(lvl == 97){ xp = 912673; }
            else if(lvl == 98){ xp = 941192; }
            else if(lvl == 99){ xp = 970299; }
            else if(lvl == 100){ xp = 1000000; }
        }
        else if (levelRate == "Medium-Slow")
        {
            if(lvl == 1){ xp = 0; }
            else if(lvl == 2){ xp = 9; }
            else if(lvl == 3){ xp = 57; }
            else if(lvl == 4){ xp = 96; }
            else if(lvl == 5){ xp = 135; }
            else if(lvl == 6){ xp = 179; }
            else if(lvl == 7){ xp = 236; }
            else if(lvl == 8){ xp = 314; }
            else if(lvl == 9){ xp = 419; }
            else if(lvl == 10){ xp = 560; }
            else if(lvl == 11){ xp = 742; }
            else if(lvl == 12){ xp = 973; }
            else if(lvl == 13){ xp = 1261; }
            else if(lvl == 14){ xp = 1612; }
            else if(lvl == 15){ xp = 2035; }
            else if(lvl == 16){ xp = 2535; }
            else if(lvl == 17){ xp = 3120; }
            else if(lvl == 18){ xp = 3798; }
            else if(lvl == 19){ xp = 4575; }
            else if(lvl == 20){ xp = 5460; }
            else if(lvl == 21){ xp = 6458; }
            else if(lvl == 22){ xp = 7577; }
            else if(lvl == 23){ xp = 8825; }
            else if(lvl == 24){ xp = 10208; }
            else if(lvl == 25){ xp = 11735; }
            else if(lvl == 26){ xp = 13411; }
            else if(lvl == 27){ xp = 15244; }
            else if(lvl == 28){ xp = 17242; }
            else if(lvl == 29){ xp = 19411; }
            else if(lvl == 30){ xp = 21760; }
            else if(lvl == 31){ xp = 24294; }
            else if(lvl == 32){ xp = 27021; }
            else if(lvl == 33){ xp = 29949; }
            else if(lvl == 34){ xp = 33084; }
            else if(lvl == 35){ xp = 36435; }
            else if(lvl == 36){ xp = 40007; }
            else if(lvl == 37){ xp = 43808; }
            else if(lvl == 38){ xp = 47846; }
            else if(lvl == 39){ xp = 52127; }
            else if(lvl == 40){ xp = 56660; }
            else if(lvl == 41){ xp = 61450; }
            else if(lvl == 42){ xp = 66505; }
            else if(lvl == 43){ xp = 71833; }
            else if(lvl == 44){ xp = 77440; }
            else if(lvl == 45){ xp = 83335; }
            else if(lvl == 46){ xp = 89523; }
            else if(lvl == 47){ xp = 96012; }
            else if(lvl == 48){ xp = 102810; }
            else if(lvl == 49){ xp = 109923; }
            else if(lvl == 50){ xp = 117360; }
            else if(lvl == 51){ xp = 125126; }
            else if(lvl == 52){ xp = 133229; }
            else if(lvl == 53){ xp = 141677; }
            else if(lvl == 54){ xp = 150476; }
            else if(lvl == 55){ xp = 159635; }
            else if(lvl == 56){ xp = 169159; }
            else if(lvl == 57){ xp = 179056; }
            else if(lvl == 58){ xp = 189334; }
            else if(lvl == 59){ xp = 199999; }
            else if(lvl == 60){ xp = 211060; }
            else if(lvl == 61){ xp = 222522; }
            else if(lvl == 62){ xp = 234393; }
            else if(lvl == 63){ xp = 246681; }
            else if(lvl == 64){ xp = 259392; }
            else if(lvl == 65){ xp = 272535; }
            else if(lvl == 66){ xp = 286115; }
            else if(lvl == 67){ xp = 300140; }
            else if(lvl == 68){ xp = 314618; }
            else if(lvl == 69){ xp = 329555; }
            else if(lvl == 70){ xp = 344960; }
            else if(lvl == 71){ xp = 360838; }
            else if(lvl == 72){ xp = 377197; }
            else if(lvl == 73){ xp = 394045; }
            else if(lvl == 74){ xp = 411388; }
            else if(lvl == 75){ xp = 429235; }
            else if(lvl == 76){ xp = 447591; }
            else if(lvl == 77){ xp = 466464; }
            else if(lvl == 78){ xp = 485862; }
            else if(lvl == 79){ xp = 505791; }
            else if(lvl == 80){ xp = 526260; }
            else if(lvl == 81){ xp = 547274; }
            else if(lvl == 82){ xp = 568841; }
            else if(lvl == 83){ xp = 590969; }
            else if(lvl == 84){ xp = 613664; }
            else if(lvl == 85){ xp = 636935; }
            else if(lvl == 86){ xp = 660787; }
            else if(lvl == 87){ xp = 685228; }
            else if(lvl == 88){ xp = 710266; }
            else if(lvl == 89){ xp = 735907; }
            else if(lvl == 90){ xp = 762160; }
            else if(lvl == 91){ xp = 789030; }
            else if(lvl == 92){ xp = 816525; }
            else if(lvl == 93){ xp = 844653; }
            else if(lvl == 94){ xp = 873420; }
            else if(lvl == 95){ xp = 902835; }
            else if(lvl == 96){ xp = 932903; }
            else if(lvl == 97){ xp = 963632; }
            else if(lvl == 98){ xp = 995030; }
            else if(lvl == 99){ xp = 1027103; }
            else if(lvl == 100){ xp = 1059860; }
        }
        else if (levelRate == "Slow")
        {
            if(lvl == 1){ xp = 0; }
            else if(lvl == 2){ xp = 10; }
            else if(lvl == 3){ xp = 33; }
            else if(lvl == 4){ xp = 80; }
            else if(lvl == 5){ xp = 156; }
            else if(lvl == 6){ xp = 270; }
            else if(lvl == 7){ xp = 428; }
            else if(lvl == 8){ xp = 640; }
            else if(lvl == 9){ xp = 911; }
            else if(lvl == 10){ xp = 1250; }
            else if(lvl == 11){ xp = 1663; }
            else if(lvl == 12){ xp = 2160; }
            else if(lvl == 13){ xp = 2746; }
            else if(lvl == 14){ xp = 3430; }
            else if(lvl == 15){ xp = 4218; }
            else if(lvl == 16){ xp = 5120; }
            else if(lvl == 17){ xp = 6141; }
            else if(lvl == 18){ xp = 7290; }
            else if(lvl == 19){ xp = 8573; }
            else if(lvl == 20){ xp = 10000; }
            else if(lvl == 21){ xp = 11576; }
            else if(lvl == 22){ xp = 13310; }
            else if(lvl == 23){ xp = 15208; }
            else if(lvl == 24){ xp = 17280; }
            else if(lvl == 25){ xp = 19531; }
            else if(lvl == 26){ xp = 21970; }
            else if(lvl == 27){ xp = 24603; }
            else if(lvl == 28){ xp = 27440; }
            else if(lvl == 29){ xp = 30486; }
            else if(lvl == 30){ xp = 33750; }
            else if(lvl == 31){ xp = 37238; }
            else if(lvl == 32){ xp = 40960; }
            else if(lvl == 33){ xp = 44921; }
            else if(lvl == 34){ xp = 49130; }
            else if(lvl == 35){ xp = 53593; }
            else if(lvl == 36){ xp = 58320; }
            else if(lvl == 37){ xp = 63316; }
            else if(lvl == 38){ xp = 68590; }
            else if(lvl == 39){ xp = 74148; }
            else if(lvl == 40){ xp = 80000; }
            else if(lvl == 41){ xp = 86151; }
            else if(lvl == 42){ xp = 92610; }
            else if(lvl == 43){ xp = 99383; }
            else if(lvl == 44){ xp = 106480; }
            else if(lvl == 45){ xp = 113906; }
            else if(lvl == 46){ xp = 121670; }
            else if(lvl == 47){ xp = 129778; }
            else if(lvl == 48){ xp = 138240; }
            else if(lvl == 49){ xp = 147061; }
            else if(lvl == 50){ xp = 156250; }
            else if(lvl == 51){ xp = 165813; }
            else if(lvl == 52){ xp = 175760; }
            else if(lvl == 53){ xp = 186096; }
            else if(lvl == 54){ xp = 196830; }
            else if(lvl == 55){ xp = 207968; }
            else if(lvl == 56){ xp = 219520; }
            else if(lvl == 57){ xp = 231491; }
            else if(lvl == 58){ xp = 243890; }
            else if(lvl == 59){ xp = 256723; }
            else if(lvl == 60){ xp = 270000; }
            else if(lvl == 61){ xp = 283726; }
            else if(lvl == 62){ xp = 297910; }
            else if(lvl == 63){ xp = 312558; }
            else if(lvl == 64){ xp = 327680; }
            else if(lvl == 65){ xp = 343281; }
            else if(lvl == 66){ xp = 359370; }
            else if(lvl == 67){ xp = 375953; }
            else if(lvl == 68){ xp = 393040; }
            else if(lvl == 69){ xp = 410636; }
            else if(lvl == 70){ xp = 428750; }
            else if(lvl == 71){ xp = 447388; }
            else if(lvl == 72){ xp = 466560; }
            else if(lvl == 73){ xp = 486271; }
            else if(lvl == 74){ xp = 506530; }
            else if(lvl == 75){ xp = 527343; }
            else if(lvl == 76){ xp = 548720; }
            else if(lvl == 77){ xp = 570666; }
            else if(lvl == 78){ xp = 593190; }
            else if(lvl == 79){ xp = 616298; }
            else if(lvl == 80){ xp = 640000; }
            else if(lvl == 81){ xp = 664301; }
            else if(lvl == 82){ xp = 689210; }
            else if(lvl == 83){ xp = 714733; }
            else if(lvl == 84){ xp = 740880; }
            else if(lvl == 85){ xp = 767656; }
            else if(lvl == 86){ xp = 795070; }
            else if(lvl == 87){ xp = 823128; }
            else if(lvl == 88){ xp = 851840; }
            else if(lvl == 89){ xp = 881211; }
            else if(lvl == 90){ xp = 911250; }
            else if(lvl == 91){ xp = 941963; }
            else if(lvl == 92){ xp = 973360; }
            else if(lvl == 93){ xp = 1005446; }
            else if(lvl == 94){ xp = 1038230; }
            else if(lvl == 95){ xp = 1071718; }
            else if(lvl == 96){ xp = 1105920; }
            else if(lvl == 97){ xp = 1140841; }
            else if(lvl == 98){ xp = 1176490; }
            else if(lvl == 99){ xp = 1212873; }
            else if(lvl == 100){ xp = 1250000; }
        }
        else if (levelRate == "Fluctuating")
        {
            if(lvl == 1){ xp = 0; }
            else if(lvl == 2){ xp = 4; }
            else if(lvl == 3){ xp = 13; }
            else if(lvl == 4){ xp = 32; }
            else if(lvl == 5){ xp = 65; }
            else if(lvl == 6){ xp = 112; }
            else if(lvl == 7){ xp = 178; }
            else if(lvl == 8){ xp = 276; }
            else if(lvl == 9){ xp = 393; }
            else if(lvl == 10){ xp = 540; }
            else if(lvl == 11){ xp = 745; }
            else if(lvl == 12){ xp = 967; }
            else if(lvl == 13){ xp = 1230; }
            else if(lvl == 14){ xp = 1591; }
            else if(lvl == 15){ xp = 1957; }
            else if(lvl == 16){ xp = 2457; }
            else if(lvl == 17){ xp = 3046; }
            else if(lvl == 18){ xp = 3732; }
            else if(lvl == 19){ xp = 4526; }
            else if(lvl == 20){ xp = 5440; }
            else if(lvl == 21){ xp = 6482; }
            else if(lvl == 22){ xp = 7666; }
            else if(lvl == 23){ xp = 9003; }
            else if(lvl == 24){ xp = 10506; }
            else if(lvl == 25){ xp = 12187; }
            else if(lvl == 26){ xp = 14060; }
            else if(lvl == 27){ xp = 16140; }
            else if(lvl == 28){ xp = 18439; }
            else if(lvl == 29){ xp = 20974; }
            else if(lvl == 30){ xp = 23760; }
            else if(lvl == 31){ xp = 26811; }
            else if(lvl == 32){ xp = 30146; }
            else if(lvl == 33){ xp = 33780; }
            else if(lvl == 34){ xp = 37731; }
            else if(lvl == 35){ xp = 42017; }
            else if(lvl == 36){ xp = 46656; }
            else if(lvl == 37){ xp = 50653; }
            else if(lvl == 38){ xp = 55969; }
            else if(lvl == 39){ xp = 60505; }
            else if(lvl == 40){ xp = 66560; }
            else if(lvl == 41){ xp = 71677; }
            else if(lvl == 42){ xp = 78533; }
            else if(lvl == 43){ xp = 84277; }
            else if(lvl == 44){ xp = 91998; }
            else if(lvl == 45){ xp = 98415; }
            else if(lvl == 46){ xp = 107069; }
            else if(lvl == 47){ xp = 114205; }
            else if(lvl == 48){ xp = 123863; }
            else if(lvl == 49){ xp = 131766; }
            else if(lvl == 50){ xp = 142500; }
            else if(lvl == 51){ xp = 151222; }
            else if(lvl == 52){ xp = 163105; }
            else if(lvl == 53){ xp = 172697; }
            else if(lvl == 54){ xp = 185807; }
            else if(lvl == 55){ xp = 196322; }
            else if(lvl == 56){ xp = 210739; }
            else if(lvl == 57){ xp = 222231; }
            else if(lvl == 58){ xp = 238036; }
            else if(lvl == 59){ xp = 250562; }
            else if(lvl == 60){ xp = 267840; }
            else if(lvl == 61){ xp = 281456; }
            else if(lvl == 62){ xp = 300293; }
            else if(lvl == 63){ xp = 315059; }
            else if(lvl == 64){ xp = 335544; }
            else if(lvl == 65){ xp = 351520; }
            else if(lvl == 66){ xp = 373744; }
            else if(lvl == 67){ xp = 390991; }
            else if(lvl == 68){ xp = 415050; }
            else if(lvl == 69){ xp = 433631; }
            else if(lvl == 70){ xp = 459620; }
            else if(lvl == 71){ xp = 479600; }
            else if(lvl == 72){ xp = 507617; }
            else if(lvl == 73){ xp = 529063; }
            else if(lvl == 74){ xp = 559209; }
            else if(lvl == 75){ xp = 582187; }
            else if(lvl == 76){ xp = 614566; }
            else if(lvl == 77){ xp = 639146; }
            else if(lvl == 78){ xp = 673863; }
            else if(lvl == 79){ xp = 700115; }
            else if(lvl == 80){ xp = 737280; }
            else if(lvl == 81){ xp = 765275; }
            else if(lvl == 82){ xp = 804997; }
            else if(lvl == 83){ xp = 834809; }
            else if(lvl == 84){ xp = 877201; }
            else if(lvl == 85){ xp = 908905; }
            else if(lvl == 86){ xp = 954084; }
            else if(lvl == 87){ xp = 987754; }
            else if(lvl == 88){ xp = 1035837; }
            else if(lvl == 89){ xp = 1071552; }
            else if(lvl == 90){ xp = 1122660; }
            else if(lvl == 91){ xp = 1160499; }
            else if(lvl == 92){ xp = 1214753; }
            else if(lvl == 93){ xp = 1254796; }
            else if(lvl == 94){ xp = 1312322; }
            else if(lvl == 95){ xp = 1354652; }
            else if(lvl == 96){ xp = 1415577; }
            else if(lvl == 97){ xp = 1460276; }
            else if(lvl == 98){ xp = 1524731; }
            else if(lvl == 99){ xp = 1571884; }
            else if(lvl == 100){ xp = 1640000; }
        }
        return xp
    }

    public static int getLevel(int xp, String levelRate)
    {
        int lvl
        if (levelRate == "Erratic")
        {
            if(xp >= 0){ lvl = 1; }
            else if(xp >= 4){ lvl = 2; }
            else if(xp >= 16){ lvl = 3; }
            else if(xp >= 38){ lvl = 4; }
            else if(xp >= 75){ lvl = 5; }
            else if(xp >= 129){ lvl = 6; }
            else if(xp >= 205){ lvl = 7; }
            else if(xp >= 307){ lvl = 8; }
            else if(xp >= 437){ lvl = 9; }
            else if(xp >= 600){ lvl = 10; }
            else if(xp >= 798){ lvl = 11; }
            else if(xp >= 1036){ lvl = 12; }
            else if(xp >= 1318){ lvl = 13; }
            else if(xp >= 1646){ lvl = 14; }
            else if(xp >= 2025){ lvl = 15; }
            else if(xp >= 2457){ lvl = 16; }
            else if(xp >= 2947){ lvl = 17; }
            else if(xp >= 3499){ lvl = 18; }
            else if(xp >= 4115){ lvl = 19; }
            else if(xp >= 4800){ lvl = 20; }
            else if(xp >= 5556){ lvl = 21; }
            else if(xp >= 6388){ lvl = 22; }
            else if(xp >= 7300){ lvl = 23; }
            else if(xp >= 8294){ lvl = 24; }
            else if(xp >= 9375){ lvl = 25; }
            else if(xp >= 10545){ lvl = 26; }
            else if(xp >= 11809){ lvl = 27; }
            else if(xp >= 13171){ lvl = 28; }
            else if(xp >= 14633){ lvl = 29; }
            else if(xp >= 16200){ lvl = 30; }
            else if(xp >= 17874){ lvl = 31; }
            else if(xp >= 19660){ lvl = 32; }
            else if(xp >= 21562){ lvl = 33; }
            else if(xp >= 23582){ lvl = 34; }
            else if(xp >= 25725){ lvl = 35; }
            else if(xp >= 27993){ lvl = 36; }
            else if(xp >= 30391){ lvl = 37; }
            else if(xp >= 32923){ lvl = 38; }
            else if(xp >= 35591){ lvl = 39; }
            else if(xp >= 38400){ lvl = 40; }
            else if(xp >= 41352){ lvl = 41; }
            else if(xp >= 44452){ lvl = 42; }
            else if(xp >= 47704){ lvl = 43; }
            else if(xp >= 51110){ lvl = 44; }
            else if(xp >= 54675){ lvl = 45; }
            else if(xp >= 58401){ lvl = 46; }
            else if(xp >= 62293){ lvl = 47; }
            else if(xp >= 66355){ lvl = 48; }
            else if(xp >= 70589){ lvl = 49; }
            else if(xp >= 75000){ lvl = 50; }
            else if(xp >= 79590){ lvl = 51; }
            else if(xp >= 84364){ lvl = 52; }
            else if(xp >= 89326){ lvl = 53; }
            else if(xp >= 94478){ lvl = 54; }
            else if(xp >= 99825){ lvl = 55; }
            else if(xp >= 105369){ lvl = 56; }
            else if(xp >= 111115){ lvl = 57; }
            else if(xp >= 117067){ lvl = 58; }
            else if(xp >= 123227){ lvl = 59; }
            else if(xp >= 129600){ lvl = 60; }
            else if(xp >= 136188){ lvl = 61; }
            else if(xp >= 142996){ lvl = 62; }
            else if(xp >= 150028){ lvl = 63; }
            else if(xp >= 157286){ lvl = 64; }
            else if(xp >= 164775){ lvl = 65; }
            else if(xp >= 172497){ lvl = 66; }
            else if(xp >= 180457){ lvl = 67; }
            else if(xp >= 188659){ lvl = 68; }
            else if(xp >= 197105){ lvl = 69; }
            else if(xp >= 205800){ lvl = 70; }
            else if(xp >= 214746){ lvl = 71; }
            else if(xp >= 223948){ lvl = 72; }
            else if(xp >= 233410){ lvl = 73; }
            else if(xp >= 243134){ lvl = 74; }
            else if(xp >= 253125){ lvl = 75; }
            else if(xp >= 263385){ lvl = 76; }
            else if(xp >= 273919){ lvl = 77; }
            else if(xp >= 284731){ lvl = 78; }
            else if(xp >= 295823){ lvl = 79; }
            else if(xp >= 307200){ lvl = 80; }
            else if(xp >= 318864){ lvl = 81; }
            else if(xp >= 330820){ lvl = 82; }
            else if(xp >= 343072){ lvl = 83; }
            else if(xp >= 355622){ lvl = 84; }
            else if(xp >= 368475){ lvl = 85; }
            else if(xp >= 381633){ lvl = 86; }
            else if(xp >= 395101){ lvl = 87; }
            else if(xp >= 408883){ lvl = 88; }
            else if(xp >= 422981){ lvl = 89; }
            else if(xp >= 437400){ lvl = 90; }
            else if(xp >= 452142){ lvl = 91; }
            else if(xp >= 467212){ lvl = 92; }
            else if(xp >= 482614){ lvl = 93; }
            else if(xp >= 498350){ lvl = 94; }
            else if(xp >= 514425){ lvl = 95; }
            else if(xp >= 530841){ lvl = 96; }
            else if(xp >= 547603){ lvl = 97; }
            else if(xp >= 564715){ lvl = 98; }
            else if(xp >= 582179){ lvl = 99; }
            else if(xp >= 600000){ lvl = 100; }
        }
        else if (levelRate == "Fast")
        {
            if(xp >= 800000){ lvl = 100; }
            else if(xp >= 776239){ lvl = 99; }
            else if(xp >= 752953){ lvl = 98; }
            else if(xp >= 730138){ lvl = 97; }
            else if(xp >= 707788){ lvl = 96; }
            else if(xp >= 685900){ lvl = 95; }
            else if(xp >= 664467){ lvl = 94; }
            else if(xp >= 643485){ lvl = 93; }
            else if(xp >= 622950){ lvl = 92; }
            else if(xp >= 602856){ lvl = 91; }
            else if(xp >= 583200){ lvl = 90; }
            else if(xp >= 563975){ lvl = 89; }
            else if(xp >= 545177){ lvl = 88; }
            else if(xp >= 526802){ lvl = 87; }
            else if(xp >= 508844){ lvl = 86; }
            else if(xp >= 491300){ lvl = 85; }
            else if(xp >= 474163){ lvl = 84; }
            else if(xp >= 457429){ lvl = 83; }
            else if(xp >= 441094){ lvl = 82; }
            else if(xp >= 425152){ lvl = 81; }
            else if(xp >= 409600){ lvl = 80; }
            else if(xp >= 394431){ lvl = 79; }
            else if(xp >= 379641){ lvl = 78; }
            else if(xp >= 365226){ lvl = 77; }
            else if(xp >= 351180){ lvl = 76; }
            else if(xp >= 337500){ lvl = 75; }
            else if(xp >= 324179){ lvl = 74; }
            else if(xp >= 311213){ lvl = 73; }
            else if(xp >= 298598){ lvl = 72; }
            else if(xp >= 286328){ lvl = 71; }
            else if(xp >= 274400){ lvl = 70; }
            else if(xp >= 262807){ lvl = 69; }
            else if(xp >= 251545){ lvl = 68; }
            else if(xp >= 240610){ lvl = 67; }
            else if(xp >= 229996){ lvl = 66; }
            else if(xp >= 219700){ lvl = 65; }
            else if(xp >= 209715){ lvl = 64; }
            else if(xp >= 200037){ lvl = 63; }
            else if(xp >= 190662){ lvl = 62; }
            else if(xp >= 181584){ lvl = 61; }
            else if(xp >= 172800){ lvl = 60; }
            else if(xp >= 164303){ lvl = 59; }
            else if(xp >= 156089){ lvl = 58; }
            else if(xp >= 148154){ lvl = 57; }
            else if(xp >= 140492){ lvl = 56; }
            else if(xp >= 133100){ lvl = 55; }
            else if(xp >= 125971){ lvl = 54; }
            else if(xp >= 119101){ lvl = 53; }
            else if(xp >= 112486){ lvl = 52; }
            else if(xp >= 106120){ lvl = 51; }
            else if(xp >= 100000){ lvl = 50; }
            else if(xp >= 94119){ lvl = 49; }
            else if(xp >= 88473){ lvl = 48; }
            else if(xp >= 83058){ lvl = 47; }
            else if(xp >= 77868){ lvl = 46; }
            else if(xp >= 72900){ lvl = 45; }
            else if(xp >= 68147){ lvl = 44; }
            else if(xp >= 63605){ lvl = 43; }
            else if(xp >= 59270){ lvl = 42; }
            else if(xp >= 55136){ lvl = 41; }
            else if(xp >= 51200){ lvl = 40; }
            else if(xp >= 47455){ lvl = 39; }
            else if(xp >= 43897){ lvl = 38; }
            else if(xp >= 40522){ lvl = 37; }
            else if(xp >= 37324){ lvl = 36; }
            else if(xp >= 34300){ lvl = 35; }
            else if(xp >= 31443){ lvl = 34; }
            else if(xp >= 28749){ lvl = 33; }
            else if(xp >= 26214){ lvl = 32; }
            else if(xp >= 23832){ lvl = 31; }
            else if(xp >= 21600){ lvl = 30; }
            else if(xp >= 19511){ lvl = 29; }
            else if(xp >= 17561){ lvl = 28; }
            else if(xp >= 15746){ lvl = 27; }
            else if(xp >= 14060){ lvl = 26; }
            else if(xp >= 12500){ lvl = 25; }
            else if(xp >= 11059){ lvl = 24; }
            else if(xp >= 9733){ lvl = 23; }
            else if(xp >= 8518){ lvl = 22; }
            else if(xp >= 7408){ lvl = 21; }
            else if(xp >= 6400){ lvl = 20; }
            else if(xp >= 5487){ lvl = 19; }
            else if(xp >= 4665){ lvl = 18; }
            else if(xp >= 3930){ lvl = 17; }
            else if(xp >= 3276){ lvl = 16; }
            else if(xp >= 2700){ lvl = 15; }
            else if(xp >= 2195){ lvl = 14; }
            else if(xp >= 1757){ lvl = 13; }
            else if(xp >= 1382){ lvl = 12; }
            else if(xp >= 1064){ lvl = 11; }
            else if(xp >= 800){ lvl = 10; }
            else if(xp >= 583){ lvl = 9; }
            else if(xp >= 409){ lvl = 8; }
            else if(xp >= 274){ lvl = 7; }
            else if(xp >= 172){ lvl = 6; }
            else if(xp >= 100){ lvl = 5; }
            else if(xp >= 51){ lvl = 4; }
            else if(xp >= 21){ lvl = 3; }
            else if(xp >= 6){ lvl = 2; }
            else if(xp >= 0){ lvl = 1; }
            return lvl;
        }
        else if (levelRate == "Medium-Fast")
        {
            if(xp >= 1000000){ lvl = 100; }
            else if(xp >= 970299){ lvl = 99; }
            else if(xp >= 941192){ lvl = 98; }
            else if(xp >= 912673){ lvl = 97; }
            else if(xp >= 884736){ lvl = 96; }
            else if(xp >= 857375){ lvl = 95; }
            else if(xp >= 830584){ lvl = 94; }
            else if(xp >= 804357){ lvl = 93; }
            else if(xp >= 778688){ lvl = 92; }
            else if(xp >= 753571){ lvl = 91; }
            else if(xp >= 729000){ lvl = 90; }
            else if(xp >= 704969){ lvl = 89; }
            else if(xp >= 681472){ lvl = 88; }
            else if(xp >= 658503){ lvl = 87; }
            else if(xp >= 636056){ lvl = 86; }
            else if(xp >= 614125){ lvl = 85; }
            else if(xp >= 592704){ lvl = 84; }
            else if(xp >= 571787){ lvl = 83; }
            else if(xp >= 551368){ lvl = 82; }
            else if(xp >= 531441){ lvl = 81; }
            else if(xp >= 512000){ lvl = 80; }
            else if(xp >= 493039){ lvl = 79; }
            else if(xp >= 474552){ lvl = 78; }
            else if(xp >= 456533){ lvl = 77; }
            else if(xp >= 438976){ lvl = 76; }
            else if(xp >= 421875){ lvl = 75; }
            else if(xp >= 405224){ lvl = 74; }
            else if(xp >= 389017){ lvl = 73; }
            else if(xp >= 373248){ lvl = 72; }
            else if(xp >= 357911){ lvl = 71; }
            else if(xp >= 343000){ lvl = 70; }
            else if(xp >= 328509){ lvl = 69; }
            else if(xp >= 314432){ lvl = 68; }
            else if(xp >= 300763){ lvl = 67; }
            else if(xp >= 287496){ lvl = 66; }
            else if(xp >= 274625){ lvl = 65; }
            else if(xp >= 262144){ lvl = 64; }
            else if(xp >= 250047){ lvl = 63; }
            else if(xp >= 238328){ lvl = 62; }
            else if(xp >= 226981){ lvl = 61; }
            else if(xp >= 216000){ lvl = 60; }
            else if(xp >= 205379){ lvl = 59; }
            else if(xp >= 195112){ lvl = 58; }
            else if(xp >= 185193){ lvl = 57; }
            else if(xp >= 175616){ lvl = 56; }
            else if(xp >= 166375){ lvl = 55; }
            else if(xp >= 157464){ lvl = 54; }
            else if(xp >= 148877){ lvl = 53; }
            else if(xp >= 140608){ lvl = 52; }
            else if(xp >= 132651){ lvl = 51; }
            else if(xp >= 125000){ lvl = 50; }
            else if(xp >= 117649){ lvl = 49; }
            else if(xp >= 110592){ lvl = 48; }
            else if(xp >= 103823){ lvl = 47; }
            else if(xp >= 97336){ lvl = 46; }
            else if(xp >= 91125){ lvl = 45; }
            else if(xp >= 85184){ lvl = 44; }
            else if(xp >= 79507){ lvl = 43; }
            else if(xp >= 74088){ lvl = 42; }
            else if(xp >= 68921){ lvl = 41; }
            else if(xp >= 64000){ lvl = 40; }
            else if(xp >= 59319){ lvl = 39; }
            else if(xp >= 54872){ lvl = 38; }
            else if(xp >= 50653){ lvl = 37; }
            else if(xp >= 46656){ lvl = 36; }
            else if(xp >= 42875){ lvl = 35; }
            else if(xp >= 39304){ lvl = 34; }
            else if(xp >= 35937){ lvl = 33; }
            else if(xp >= 32768){ lvl = 32; }
            else if(xp >= 29791){ lvl = 31; }
            else if(xp >= 27000){ lvl = 30; }
            else if(xp >= 24389){ lvl = 29; }
            else if(xp >= 21952){ lvl = 28; }
            else if(xp >= 19683){ lvl = 27; }
            else if(xp >= 17576){ lvl = 26; }
            else if(xp >= 15625){ lvl = 25; }
            else if(xp >= 13824){ lvl = 24; }
            else if(xp >= 12167){ lvl = 23; }
            else if(xp >= 10648){ lvl = 22; }
            else if(xp >= 9261){ lvl = 21; }
            else if(xp >= 8000){ lvl = 20; }
            else if(xp >= 6859){ lvl = 19; }
            else if(xp >= 5832){ lvl = 18; }
            else if(xp >= 4913){ lvl = 17; }
            else if(xp >= 4096){ lvl = 16; }
            else if(xp >= 3375){ lvl = 15; }
            else if(xp >= 2744){ lvl = 14; }
            else if(xp >= 2197){ lvl = 13; }
            else if(xp >= 1728){ lvl = 12; }
            else if(xp >= 1331){ lvl = 11; }
            else if(xp >= 1000){ lvl = 10; }
            else if(xp >= 729){ lvl = 9; }
            else if(xp >= 512){ lvl = 8; }
            else if(xp >= 343){ lvl = 7; }
            else if(xp >= 216){ lvl = 6; }
            else if(xp >= 125){ lvl = 5; }
            else if(xp >= 64){ lvl = 4; }
            else if(xp >= 27){ lvl = 3; }
            else if(xp >= 8){ lvl = 2; }
            else if(xp >= 0){ lvl = 1; }
        }
        else if (levelRate == "Medium-Slow")
        {
            if(xp >= 1059860){ lvl = 100; }
            else if(xp >= 1027103){ lvl = 99; }
            else if(xp >= 995030){ lvl = 98; }
            else if(xp >= 963632){ lvl = 97; }
            else if(xp >= 932903){ lvl = 96; }
            else if(xp >= 902835){ lvl = 95; }
            else if(xp >= 873420){ lvl = 94; }
            else if(xp >= 844653){ lvl = 93; }
            else if(xp >= 816525){ lvl = 92; }
            else if(xp >= 789030){ lvl = 91; }
            else if(xp >= 762160){ lvl = 90; }
            else if(xp >= 735907){ lvl = 89; }
            else if(xp >= 710266){ lvl = 88; }
            else if(xp >= 685228){ lvl = 87; }
            else if(xp >= 660787){ lvl = 86; }
            else if(xp >= 636935){ lvl = 85; }
            else if(xp >= 613664){ lvl = 84; }
            else if(xp >= 590969){ lvl = 83; }
            else if(xp >= 568841){ lvl = 82; }
            else if(xp >= 547274){ lvl = 81; }
            else if(xp >= 526260){ lvl = 80; }
            else if(xp >= 505791){ lvl = 79; }
            else if(xp >= 485862){ lvl = 78; }
            else if(xp >= 466464){ lvl = 77; }
            else if(xp >= 447591){ lvl = 76; }
            else if(xp >= 429235){ lvl = 75; }
            else if(xp >= 411388){ lvl = 74; }
            else if(xp >= 394045){ lvl = 73; }
            else if(xp >= 377197){ lvl = 72; }
            else if(xp >= 360838){ lvl = 71; }
            else if(xp >= 344960){ lvl = 70; }
            else if(xp >= 329555){ lvl = 69; }
            else if(xp >= 314618){ lvl = 68; }
            else if(xp >= 300140){ lvl = 67; }
            else if(xp >= 286115){ lvl = 66; }
            else if(xp >= 272535){ lvl = 65; }
            else if(xp >= 259392){ lvl = 64; }
            else if(xp >= 246681){ lvl = 63; }
            else if(xp >= 234393){ lvl = 62; }
            else if(xp >= 222522){ lvl = 61; }
            else if(xp >= 211060){ lvl = 60; }
            else if(xp >= 199999){ lvl = 59; }
            else if(xp >= 189334){ lvl = 58; }
            else if(xp >= 179056){ lvl = 57; }
            else if(xp >= 169159){ lvl = 56; }
            else if(xp >= 159635){ lvl = 55; }
            else if(xp >= 150476){ lvl = 54; }
            else if(xp >= 141677){ lvl = 53; }
            else if(xp >= 133229){ lvl = 52; }
            else if(xp >= 125126){ lvl = 51; }
            else if(xp >= 117360){ lvl = 50; }
            else if(xp >= 109923){ lvl = 49; }
            else if(xp >= 102810){ lvl = 48; }
            else if(xp >= 96012){ lvl = 47; }
            else if(xp >= 89523){ lvl = 46; }
            else if(xp >= 83335){ lvl = 45; }
            else if(xp >= 77440){ lvl = 44; }
            else if(xp >= 71833){ lvl = 43; }
            else if(xp >= 66505){ lvl = 42; }
            else if(xp >= 61450){ lvl = 41; }
            else if(xp >= 56660){ lvl = 40; }
            else if(xp >= 52127){ lvl = 39; }
            else if(xp >= 47846){ lvl = 38; }
            else if(xp >= 43808){ lvl = 37; }
            else if(xp >= 40007){ lvl = 36; }
            else if(xp >= 36435){ lvl = 35; }
            else if(xp >= 33084){ lvl = 34; }
            else if(xp >= 29949){ lvl = 33; }
            else if(xp >= 27021){ lvl = 32; }
            else if(xp >= 24294){ lvl = 31; }
            else if(xp >= 21760){ lvl = 30; }
            else if(xp >= 19411){ lvl = 29; }
            else if(xp >= 17242){ lvl = 28; }
            else if(xp >= 15244){ lvl = 27; }
            else if(xp >= 13411){ lvl = 26; }
            else if(xp >= 11735){ lvl = 25; }
            else if(xp >= 10208){ lvl = 24; }
            else if(xp >= 8825){ lvl = 23; }
            else if(xp >= 7577){ lvl = 22; }
            else if(xp >= 6458){ lvl = 21; }
            else if(xp >= 5460){ lvl = 20; }
            else if(xp >= 4575){ lvl = 19; }
            else if(xp >= 3798){ lvl = 18; }
            else if(xp >= 3120){ lvl = 17; }
            else if(xp >= 2535){ lvl = 16; }
            else if(xp >= 2035){ lvl = 15; }
            else if(xp >= 1612){ lvl = 14; }
            else if(xp >= 1261){ lvl = 13; }
            else if(xp >= 973){ lvl = 12; }
            else if(xp >= 742){ lvl = 11; }
            else if(xp >= 560){ lvl = 10; }
            else if(xp >= 419){ lvl = 9; }
            else if(xp >= 314){ lvl = 8; }
            else if(xp >= 236){ lvl = 7; }
            else if(xp >= 179){ lvl = 6; }
            else if(xp >= 135){ lvl = 5; }
            else if(xp >= 96){ lvl = 4; }
            else if(xp >= 57){ lvl = 3; }
            else if(xp >= 9){ lvl = 2; }
            else if(xp >= 0){ lvl = 1; }
        }
        else if (levelRate == "Slow")
        {
            if(xp >= 1250000){ lvl = 100; }
            else if(xp >= 1212873){ lvl = 99; }
            else if(xp >= 1176490){ lvl = 98; }
            else if(xp >= 1140841){ lvl = 97; }
            else if(xp >= 1105920){ lvl = 96; }
            else if(xp >= 1071718){ lvl = 95; }
            else if(xp >= 1038230){ lvl = 94; }
            else if(xp >= 1005446){ lvl = 93; }
            else if(xp >= 973360){ lvl = 92; }
            else if(xp >= 941963){ lvl = 91; }
            else if(xp >= 911250){ lvl = 90; }
            else if(xp >= 881211){ lvl = 89; }
            else if(xp >= 851840){ lvl = 88; }
            else if(xp >= 823128){ lvl = 87; }
            else if(xp >= 795070){ lvl = 86; }
            else if(xp >= 767656){ lvl = 85; }
            else if(xp >= 740880){ lvl = 84; }
            else if(xp >= 714733){ lvl = 83; }
            else if(xp >= 689210){ lvl = 82; }
            else if(xp >= 664301){ lvl = 81; }
            else if(xp >= 640000){ lvl = 80; }
            else if(xp >= 616298){ lvl = 79; }
            else if(xp >= 593190){ lvl = 78; }
            else if(xp >= 570666){ lvl = 77; }
            else if(xp >= 548720){ lvl = 76; }
            else if(xp >= 527343){ lvl = 75; }
            else if(xp >= 506530){ lvl = 74; }
            else if(xp >= 486271){ lvl = 73; }
            else if(xp >= 466560){ lvl = 72; }
            else if(xp >= 447388){ lvl = 71; }
            else if(xp >= 428750){ lvl = 70; }
            else if(xp >= 410636){ lvl = 69; }
            else if(xp >= 393040){ lvl = 68; }
            else if(xp >= 375953){ lvl = 67; }
            else if(xp >= 359370){ lvl = 66; }
            else if(xp >= 343281){ lvl = 65; }
            else if(xp >= 327680){ lvl = 64; }
            else if(xp >= 312558){ lvl = 63; }
            else if(xp >= 297910){ lvl = 62; }
            else if(xp >= 283726){ lvl = 61; }
            else if(xp >= 270000){ lvl = 60; }
            else if(xp >= 256723){ lvl = 59; }
            else if(xp >= 243890){ lvl = 58; }
            else if(xp >= 231491){ lvl = 57; }
            else if(xp >= 219520){ lvl = 56; }
            else if(xp >= 207968){ lvl = 55; }
            else if(xp >= 196830){ lvl = 54; }
            else if(xp >= 186096){ lvl = 53; }
            else if(xp >= 175760){ lvl = 52; }
            else if(xp >= 165813){ lvl = 51; }
            else if(xp >= 156250){ lvl = 50; }
            else if(xp >= 147061){ lvl = 49; }
            else if(xp >= 138240){ lvl = 48; }
            else if(xp >= 129778){ lvl = 47; }
            else if(xp >= 121670){ lvl = 46; }
            else if(xp >= 113906){ lvl = 45; }
            else if(xp >= 106480){ lvl = 44; }
            else if(xp >= 99383){ lvl = 43; }
            else if(xp >= 92610){ lvl = 42; }
            else if(xp >= 86151){ lvl = 41; }
            else if(xp >= 80000){ lvl = 40; }
            else if(xp >= 74148){ lvl = 39; }
            else if(xp >= 68590){ lvl = 38; }
            else if(xp >= 63316){ lvl = 37; }
            else if(xp >= 58320){ lvl = 36; }
            else if(xp >= 53593){ lvl = 35; }
            else if(xp >= 49130){ lvl = 34; }
            else if(xp >= 44921){ lvl = 33; }
            else if(xp >= 40960){ lvl = 32; }
            else if(xp >= 37238){ lvl = 31; }
            else if(xp >= 33750){ lvl = 30; }
            else if(xp >= 30486){ lvl = 29; }
            else if(xp >= 27440){ lvl = 28; }
            else if(xp >= 24603){ lvl = 27; }
            else if(xp >= 21970){ lvl = 26; }
            else if(xp >= 19531){ lvl = 25; }
            else if(xp >= 17280){ lvl = 24; }
            else if(xp >= 15208){ lvl = 23; }
            else if(xp >= 13310){ lvl = 22; }
            else if(xp >= 11576){ lvl = 21; }
            else if(xp >= 10000){ lvl = 20; }
            else if(xp >= 8573){ lvl = 19; }
            else if(xp >= 7290){ lvl = 18; }
            else if(xp >= 6141){ lvl = 17; }
            else if(xp >= 5120){ lvl = 16; }
            else if(xp >= 4218){ lvl = 15; }
            else if(xp >= 3430){ lvl = 14; }
            else if(xp >= 2746){ lvl = 13; }
            else if(xp >= 2160){ lvl = 12; }
            else if(xp >= 1663){ lvl = 11; }
            else if(xp >= 1250){ lvl = 10; }
            else if(xp >= 911){ lvl = 9; }
            else if(xp >= 640){ lvl = 8; }
            else if(xp >= 428){ lvl = 7; }
            else if(xp >= 270){ lvl = 6; }
            else if(xp >= 156){ lvl = 5; }
            else if(xp >= 80){ lvl = 4; }
            else if(xp >= 33){ lvl = 3; }
            else if(xp >= 10){ lvl = 2; }
            else if(xp >= 0){ lvl = 1; }

        }
        else if (levelRate == "Fluctuating")
        {
            if(xp >= 1640000){ lvl = 100; }
            else if(xp >= 1571884){ lvl = 99; }
            else if(xp >= 1524731){ lvl = 98; }
            else if(xp >= 1460276){ lvl = 97; }
            else if(xp >= 1415577){ lvl = 96; }
            else if(xp >= 1354652){ lvl = 95; }
            else if(xp >= 1312322){ lvl = 94; }
            else if(xp >= 1254796){ lvl = 93; }
            else if(xp >= 1214753){ lvl = 92; }
            else if(xp >= 1160499){ lvl = 91; }
            else if(xp >= 1122660){ lvl = 90; }
            else if(xp >= 1071552){ lvl = 89; }
            else if(xp >= 1035837){ lvl = 88; }
            else if(xp >= 987754){ lvl = 87; }
            else if(xp >= 954084){ lvl = 86; }
            else if(xp >= 908905){ lvl = 85; }
            else if(xp >= 877201){ lvl = 84; }
            else if(xp >= 834809){ lvl = 83; }
            else if(xp >= 804997){ lvl = 82; }
            else if(xp >= 765275){ lvl = 81; }
            else if(xp >= 737280){ lvl = 80; }
            else if(xp >= 700115){ lvl = 79; }
            else if(xp >= 673863){ lvl = 78; }
            else if(xp >= 639146){ lvl = 77; }
            else if(xp >= 614566){ lvl = 76; }
            else if(xp >= 582187){ lvl = 75; }
            else if(xp >= 559209){ lvl = 74; }
            else if(xp >= 529063){ lvl = 73; }
            else if(xp >= 507617){ lvl = 72; }
            else if(xp >= 479600){ lvl = 71; }
            else if(xp >= 459620){ lvl = 70; }
            else if(xp >= 433631){ lvl = 69; }
            else if(xp >= 415050){ lvl = 68; }
            else if(xp >= 390991){ lvl = 67; }
            else if(xp >= 373744){ lvl = 66; }
            else if(xp >= 351520){ lvl = 65; }
            else if(xp >= 335544){ lvl = 64; }
            else if(xp >= 315059){ lvl = 63; }
            else if(xp >= 300293){ lvl = 62; }
            else if(xp >= 281456){ lvl = 61; }
            else if(xp >= 267840){ lvl = 60; }
            else if(xp >= 250562){ lvl = 59; }
            else if(xp >= 238036){ lvl = 58; }
            else if(xp >= 222231){ lvl = 57; }
            else if(xp >= 210739){ lvl = 56; }
            else if(xp >= 196322){ lvl = 55; }
            else if(xp >= 185807){ lvl = 54; }
            else if(xp >= 172697){ lvl = 53; }
            else if(xp >= 163105){ lvl = 52; }
            else if(xp >= 151222){ lvl = 51; }
            else if(xp >= 142500){ lvl = 50; }
            else if(xp >= 131766){ lvl = 49; }
            else if(xp >= 123863){ lvl = 48; }
            else if(xp >= 114205){ lvl = 47; }
            else if(xp >= 107069){ lvl = 46; }
            else if(xp >= 98415){ lvl = 45; }
            else if(xp >= 91998){ lvl = 44; }
            else if(xp >= 84277){ lvl = 43; }
            else if(xp >= 78533){ lvl = 42; }
            else if(xp >= 71677){ lvl = 41; }
            else if(xp >= 66560){ lvl = 40; }
            else if(xp >= 60505){ lvl = 39; }
            else if(xp >= 55969){ lvl = 38; }
            else if(xp >= 50653){ lvl = 37; }
            else if(xp >= 46656){ lvl = 36; }
            else if(xp >= 42017){ lvl = 35; }
            else if(xp >= 37731){ lvl = 34; }
            else if(xp >= 33780){ lvl = 33; }
            else if(xp >= 30146){ lvl = 32; }
            else if(xp >= 26811){ lvl = 31; }
            else if(xp >= 23760){ lvl = 30; }
            else if(xp >= 20974){ lvl = 29; }
            else if(xp >= 18439){ lvl = 28; }
            else if(xp >= 16140){ lvl = 27; }
            else if(xp >= 14060){ lvl = 26; }
            else if(xp >= 12187){ lvl = 25; }
            else if(xp >= 10506){ lvl = 24; }
            else if(xp >= 9003){ lvl = 23; }
            else if(xp >= 7666){ lvl = 22; }
            else if(xp >= 6482){ lvl = 21; }
            else if(xp >= 5440){ lvl = 20; }
            else if(xp >= 4526){ lvl = 19; }
            else if(xp >= 3732){ lvl = 18; }
            else if(xp >= 3046){ lvl = 17; }
            else if(xp >= 2457){ lvl = 16; }
            else if(xp >= 1957){ lvl = 15; }
            else if(xp >= 1591){ lvl = 14; }
            else if(xp >= 1230){ lvl = 13; }
            else if(xp >= 967){ lvl = 12; }
            else if(xp >= 745){ lvl = 11; }
            else if(xp >= 540){ lvl = 10; }
            else if(xp >= 393){ lvl = 9; }
            else if(xp >= 276){ lvl = 8; }
            else if(xp >= 178){ lvl = 7; }
            else if(xp >= 112){ lvl = 6; }
            else if(xp >= 65){ lvl = 5; }
            else if(xp >= 32){ lvl = 4; }
            else if(xp >= 13){ lvl = 3; }
            else if(xp >= 4){ lvl = 2; }
            else if(xp >= 0){ lvl = 1; }
        }
        return lvl;
    }

}
