call mvn clean package
call jpackage ^
    --type msi ^
    --verbose ^
    --dest target\image ^
    --name CrystalKeep ^
    --icon cluster_node_icon.ico ^
    --module crystalkeep/nl.andrewlalis.crystalkeep.CrystalKeep ^
    --module-path target\modules ^
    --module-path target\classes ^
    --win-menu ^
    --win-per-user-install ^
    --win-shortcut ^