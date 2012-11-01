package game.context

class MoveInfo {

    // Times the attack hits
    int loop = 1
    // Should we calculate the damage
    boolean calculateDamage = true
    // Pre set damage
    Integer damage = null
    // Should we use default attack power
    boolean defaultAttackPower = true

    Integer attackPower = null
    // Default critical hit stage
    int criticalHitStage = 1
    int addToAccuracyStage = 0
    boolean canNotUseAction = false
    boolean oneHitKO = false
    boolean cantMiss = false
    boolean recoil = false
    boolean flinch = false
    boolean paralysisActionSucces = false
    boolean confusionActionSucces = false
    boolean sleepActionSucces = false
    boolean freezeActionSucces = false
    boolean flinchAction = false
    boolean sleepAction = false
    boolean confusionAction = false
    boolean effectSucces = false
    boolean burnAction = false
    boolean freezeAction = false
    boolean paralysisAction = false
    boolean poisonAction = false
    boolean badlypoisondAction = false
    int addToDefenseStage = 0
    boolean selfStageAction = false
    boolean openentStageAction = false
    int addToAttackStage = 0
    int addToSpDefenseStage = 0
    int addToSpAttackStage = 0
    int addToSpeedStage = 0
    double effectiveness = 1
    boolean effectAction = false
    boolean stageAction = false
    boolean holdMove = false
    boolean continueMove = false
    boolean doPhysicalDamage = false
    boolean recoverAction = false
    boolean badlypoisondActionSucces = false
    boolean poisonActionSucces = false
    boolean burnActionSucces = false
    boolean effectActionOnBoth = false
    int recover = 0
    int accuracy
}
