$body = @{ companyName='Test Corp'; adminEmail='admin@testcorp.com'; password='securepass'; domain='testcorp.com' } | ConvertTo-Json
Invoke-RestMethod -Uri 'http://localhost:8080/api/tenants/onboard' -Method Post -Body $body -ContentType 'application/json'
