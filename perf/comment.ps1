param(
  $baseline,
  $current,
  $repo,
  $pr,
  $token
)

# Read CSV
$base = Import-Csv $baseline
$cur  = Import-Csv $current

$baseAvg = ($base.elapsed | Measure-Object -Average).Average
$curAvg  = ($cur.elapsed  | Measure-Object -Average).Average

$diff = [math]::Round((($curAvg - $baseAvg) / $baseAvg) * 100,2)

# Build markdown safely
$comment = @"
## 🚀 Performance Report

| Metric | Value |
|--------|-------|
| Baseline Avg | $([int]$baseAvg) ms |
| Current Avg | $([int]$curAvg) ms |
| Difference | $diff % |

📊 Trend CSV attached in Jenkins artifacts.

"@

$payload = @{
  body = $comment
} | ConvertTo-Json -Depth 5

Invoke-RestMethod `
  -Uri "https://api.github.com/repos/$repo/issues/$pr/comments" `
  -Headers @{
     Authorization = "Bearer $token"
     Accept        = "application/vnd.github+json"
     "User-Agent" = "Jenkins"
  } `
  -Method POST `
  -Body $payload
