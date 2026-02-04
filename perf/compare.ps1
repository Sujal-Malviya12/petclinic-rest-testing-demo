param($baseline,$current,$threshold)

$b = Import-Csv $baseline
$c = Import-Csv $current

$baseAvg = ($b.elapsed | Measure-Object -Average).Average
$newAvg  = ($c.elapsed | Measure-Object -Average).Average

$diff = (($newAvg - $baseAvg) / $baseAvg) * 100

Write-Host "Baseline avg: $baseAvg"
Write-Host "Current avg: $newAvg"
Write-Host "Diff %: $diff"

if ($diff -gt $threshold) {
    Write-Error "Performance regression detected"
    exit 1
}
