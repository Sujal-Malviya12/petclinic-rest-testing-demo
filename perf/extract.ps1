param(
    [string]$result,
    [string]$trend
)

$line = Get-Content $result | Select-String "elapsed"

if (!$line) {
    "timestamp,avg" | Out-File $trend
    exit 0
}

$avg = (Import-Csv $result | Measure-Object elapsed -Average).Average

if (!(Test-Path $trend)) {
    "timestamp,avg" | Out-File $trend
}

"$((Get-Date).ToString('yyyy-MM-dd HH:mm:ss')),$avg" >> $trend
