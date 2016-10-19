## Movie poster scraper
Her ligger kode for å scrape [IMP Awards](http://www.impawards.com/) sine nettsider for filmplakater. Disse vil etterhvert bli lenket mot hverandre med data fra linked movie database.

Slik starter du programmet:

1. Pass på at du har MySQL kjørende på maskinen.
2. Opprette en database og kall den movieposters
3. Lag en tabell kalt Posters med følgende felt: id (auto increment), name (varchar 255), url (varchar 255), largePosterUrl (varchar 255), year (char 4).
4. Pass på at du har JSoup og MySQL-connector lagt til som dependencies.
5. Kjør programmet. Det tar litt tid å gå gjennom alle filmplakater mellom 2016 og 1912, så gå og lag deg en kopp kaffe mens databasen blir fylt opp.
