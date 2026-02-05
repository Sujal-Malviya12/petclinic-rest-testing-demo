$csv = Import-Csv perf\history\trend.csv

$html = @"
<html>
<head>
<title>Performance Dashboard</title>
</head>
<body>
<h2>Performance History</h2>
<table border="1" cellpadding="6">
<tr><th>Build</th><th>Response (ms)</th></tr>
"@

foreach($r in $csv){
  $html += "<tr><td>$($r.build)</td><td>$($r.avg)</td></tr>"
}

$html += "</table></body></html>"

$html | Out-File perf\dashboard.html
