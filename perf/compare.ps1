param($base,$current,$threshold)

function Get-Median($file) {
    $values = Import-Csv $file | Select-Object -ExpandProperty elapsed | Sort-Object
    $count = $values.Count

    if ($count -eq 0) { return 0 }

    if ($count % 2 -eq 0) {
        return ($values[$count/2] + $values[$count/2 - 1]) / 2
    } else {
        return $values[($count - 1) / 2]
    }
}

$baseline = Get-Median $base
$currentVal = Get-Median $current

if ($baseline -eq 0) {
    Write-Host "No baseline found. Skipping regression."
    exit 0
}

$diff = [math]::Round((($currentVal - $baseline) / $baseline) * 100, 2)

Write-Host "Baseline median: $baseline ms"
Write-Host "Current median: $currentVal ms"
Write-Host "Diff %: $diff"

if ($diff -gt $threshold) {
    Write-Error "Performance degraded by $diff%"
    exit 1
}

Write-Host "Performance OK"
