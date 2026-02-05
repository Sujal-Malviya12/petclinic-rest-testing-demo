$result = Import-Csv result.csv

# Jenkins build number (auto)
$build = $env:BUILD_NUMBER

# Average response time
$avg = [math]::Round(($result.elapsed | Measure-Object -Average).Average,2)

# Median
$sorted = $result.elapsed | Sort-Object
$median = $sorted[[int]($sorted.Count/2)]

$html = @"
<html>
<head>
<title>Performance Dashboard</title>
<style>
table { border-collapse: collapse; width:50%; }
th,td { border:1px solid black; padding:8px; }
th { background:#eee; }
</style>
</head>

<body>
<h2>Jenkins Performance Dashboard</h2>

<table>
<tr>
<th>Build</th>
<th>Average (ms)</th>
<th>Median (ms)</th>
</tr>

<tr>
<td>$build</td>
<td>$avg</td>
<td>$median</td>
</tr>

</table>
</body>
</html>
"@

$html | Out-File perf/dashboard.html
