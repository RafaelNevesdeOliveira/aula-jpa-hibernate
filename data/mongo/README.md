# MongoDB (sem Docker)

```properties
aula.banco=mongo
```

Guia completo: [../../README.md](../../README.md) (Forma 3).

## Mac

```sh
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
mvn -q compile exec:java
```

## Windows / Caixa

1. MSI: https://www.mongodb.com/try/download/community (Windows, msi)
2. Compass: https://www.mongodb.com/try/download/compass
3. Marque **Install as a Service** e o Compass
4. `services.msc` → MongoDB Server → Running

```powershell
Get-Service MongoDB
Start-Service MongoDB
# em data\postgres\aula.properties → aula.banco=mongo
mvn -q compile exec:java
```

Compass: `mongodb://localhost:27017` → database `aula11`

```powershell
& "C:\Program Files\MongoDB\Server\8.0\bin\mongosh.exe" mongodb://localhost:27017/aula11
```

```javascript
db.conta.find()
db.pessoa.find()
```

| Campo | Valor |
| --- | --- |
| Host | `localhost` |
| Porta | `27017` |
| Database | `aula11` |
| URI | `mongodb://localhost:27017` |
