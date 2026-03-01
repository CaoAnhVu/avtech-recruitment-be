$body = @{ companyName='Test Corp'; adminEmail='admin@testcorp.com'; adminPassword='securepass'; domain='testcorp.com' } | ConvertTo-Json
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/api/public/tenants/register' -Method Post -Body $body -ContentType 'application/json'
$response | ConvertTo-Json
