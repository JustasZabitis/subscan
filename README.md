# SubScan

SubScan finds subscriptions and recurring payments in Irish bank statements. You export a CSV from your bank, upload it, and it shows what you're paying for, how often, and how much it adds up to each month and year. It also picks up price increases, free trials that turned into paid ones, and duplicates.

Final Year Project for BSc (Hons) Software with AI & Cloud at TUS Athlone.
Justas Zabitis, supervised by Peter Vargovcik.

## Main focus

The main part of the project is the detection engine. I'm building two versions, a basic rule-based one and an improved one that uses fuzzy matching and a confidence score, and comparing how accurate they are on labelled test data. The aim is at least 90% precision and 85% recall.

## How it works

1. The user uploads a bank CSV
2. The bank is detected from the header row
3. Each row is turned into a standard transaction
4. Merchant names are cleaned up (e.g. `PAYPAL *NETFLIX.COM` becomes `Netflix`)
5. Transactions are grouped by merchant and checked for weekly, monthly or yearly patterns
6. Results are saved and shown to the user

The CSV file itself is never stored.

## Architecture

```
Expo app  →  Spring Boot REST API  →  Supabase (PostgreSQL)
             (controllers → services → repositories using JDBC)
```

- **App:** Expo (React Native) so it works on web, iPhone and Android
- **Backend:** Java 21 and Spring Boot
- **Database:** PostgreSQL on Supabase, accessed with JDBC and my own SQL
- **Login:** Supabase Auth
- **Hosting:** Render
- **CI:** GitHub Actions builds and runs the tests on every push

## Banks

- AIB (in progress)
- Bank of Ireland (in progress)
- Revolut and PTSB (planned)

## Running it locally

You need Java 21 and a Supabase project with the schema set up.

Set these environment variables (I use the IntelliJ run configuration):

```
DB_URL=jdbc:postgresql://<your-pooler-host>:5432/postgres
DB_USER=postgres.<your-project-ref>
DB_PASSWORD=<your-password>
```

Use the Session pooler connection from Supabase, not the direct one.

Then run `BackendApplication`, or from the `backend` folder:

```
mvnw.cmd spring-boot:run
```

Check it's connected at http://localhost:8080/api/health/db

## Tests

```
mvnw.cmd test
```
-TO BE DONE
Tests are written with JUnit and also run automatically on GitHub Actions.

## Project management

I'm using GitHub Issues for user stories and a GitHub Projects board with two-week sprints.

## Status

Sprint 1 (Nov 2026) is done. The backend is connected to Supabase, CI runs the tests on every pull request, and SubScan can read AIB and Bank of Ireland CSV exports, detect which bank a file came from, and clean up merchant names. The Sprint 1 go / no-go check on real statements found all the known subscriptions, so the project is going ahead as planned.

Next (Sprint 2): the first version of the subscription detection engine.
