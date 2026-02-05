param(
    [string]$csv,
    [string]$out
)

# Skip header and read elapsed column (2nd column)
$times = Import-Csv $csv | ForEach-Object { [int]$_.elapsed }

$avg = [Math]::Round(($times | Measure-Object -Average).Average, 2)

$date = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

"$date,$avg" | Out-File -Append $out

Write-Host "Recorded avg = $avg ms"
