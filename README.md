#№ JDBC

Пример работы с JDBC. В PostgreSQL было создано 3 таблицы: Provider, Provider_curr, Provider_country. У таблицы Provider отношение к двум другим таблицам Many to Many, поэтому также созданы 2 таблицы ссылок. (Стрелочка от provcurr к provider нарисована не в ту сторону, должна быть развернута).

![alt text](https://image.ibb.co/dSwL45/table.png)

Реализована вставка всех валют из enum'ов в Countries и Currencies (CountryDBWorker, CurrencyDBWorker). 
Реализована вставка экземпляров класса Provider (ProviderDBWorker) в таблицу Provider, а также вставка ссылок в таблицы ссылок на соответствующие ему валюты и страны. Реализована выборка всех провайдеров и парсинг их в лист экземпляров класса Provider (ProviderDBWorker.select). Также реализована выборка по валюте и по стране.
