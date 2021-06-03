call mvn clean package
call jpackage ^
    --type app-image ^
    --verbose ^
    --dest target\image ^
    --name CrystalKeep ^
    --icon src/main/resources/nl/andrewlalis/crystalkeep/ui/images/cluster_node_icon.png ^
    --module crystalkeep/nl.andrewlalis.crystalkeep.CrystalKeep ^
    --module-path target\modules ^
    --module-path target\classes ^
    --win-console ^