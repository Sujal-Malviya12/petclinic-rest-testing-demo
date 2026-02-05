param(
  [string]$baseline,
  [string]$current,
  [string]$repo,
  [string]$pr,
  [string]$token
)

function Get-Median($file) {
    $values = Import-Csv $file | Select -Expand elapsed | Sort
    $c = $values.Count
    if ($c -eq 0) { return 0 }
    if ($c % 2 -eq 0) {
        return ($values[$c/2] + $values[$c/2 - 1]) / 2
    } else {
        return $values[($c - 1) / 2]
    }
}

$base = Get-Median $baseline
$curr = Get-Median $current
$diff = [math]::Round((($curr-$base)/$base)*100,2)

$status = if ($diff -gt 0) { "🐢 Slower" } else { "🚀 Faster" }

$body = @{
  body = "📊 **Performance Report**

Baseline (master): $base ms  
Current PR: $curr ms  
Change: $diff %

Status: $status
"
} | ConvertTo-Json

Invoke-RestMethod `
 -Uri "https://api.github.com/repos/$repo/issues/$pr/comments" `
 -Headers @{
   Authorization = "token $token"
   Accept="application/vnd.github+json"
 } `
 -Method POST `
 -Body $body
