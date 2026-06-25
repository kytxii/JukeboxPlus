# Builds every Stonecutter version's jar into build/libs/.
# Stonecutter only merges the *active* version's source, so we must switch + build
# each version in turn (a plain `gradlew build` would only produce the active one).
# Usage:  .\build-all.ps1

$ErrorActionPreference = 'Stop'
$versions = '1.19.4', '1.20.1', '1.20.4', '1.20.6', '1.21.1', '1.21.4'

Remove-Item 'build/libs/*.jar' -ErrorAction SilentlyContinue

foreach ($v in $versions) {
    Write-Host "==> Building $v" -ForegroundColor Cyan
    & '.\gradlew.bat' "Set active project to $v" --console=plain
    if (-not $?) { throw "Failed to switch active project to $v" }
    & '.\gradlew.bat' ":${v}:buildAndCollect" --console=plain
    if (-not $?) { throw "Build failed for $v" }
}

# Leave the tree on the vcs default so the working copy is clean to commit.
& '.\gradlew.bat' 'Set active project to 1.20.1' --console=plain

Write-Host "`nDone. Jars in build/libs:" -ForegroundColor Green
Get-ChildItem 'build/libs/*.jar' |
    Where-Object { $_.Name -notlike '*sources*' } |
    ForEach-Object { Write-Host "  $($_.Name)" }
