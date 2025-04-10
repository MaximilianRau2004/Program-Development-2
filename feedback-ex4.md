# Bewertung des 4. Arbeitsblatts

> Das Arbeitsblatt besteht aus 70 Punkten, von denen 35 zum Bestehen notwendig sind (4,0).

## Übersicht der erreichten Punkte

| Kriterium                    | erreichte Punkte / maximale Punkte |
| ---------------------------- |------------------------------------|
| Funktionalität der API       | 25 / 25                            |
| DB-Persistenz                | 3 / 3                              |
| Funktionalität des Frontends | 21 / 22                            |
| Usability                    | 3 / 5                              |
| Design-Qualität              | 5 / 5                              |
| Code-Qualität                | 5 / 5                              |
| Repository und README        | 4,5 / 5                            |
| **Summe**:                   | 66,5 / 70                          |
| **Note**:                    | 1,3                                |


## Funktionalität der API (__ / 25)
> Anforderungen für maximale Punktzahl: API hat alle geforderten Ressourcen (URLs plus Request und Response Bodies) mit korrekter Funktionalität, d.h., alle [automatisierten API-Tests](https://github.tik.uni-stuttgart.de/ISTE-ESE/pe2-api-tests-ex4) wurden bestanden.
> Pro bestandenem Testfall gibt es eine bestimmte Menge an Punkten (siehe unten).

| Suite                 | # of Tests | # of Passed | # of Failed |
| --------------------- | ---------- | ----------- | ----------- |
| `assignees.tests.js`: | 11         | 11          | 0           |
| `todos.tests.js`:     | 13         | 13          | 0           |

Detaillierte Auswertung für alle API-Tests:

- Suite `assignees.tests.js` (9,5 / 9,5 Punkte)
  - [x] (1 Punkt) Test `create a valid assignee (201, id returned)`
  - [x] (1 Punkt) Test `retrieve all assignees (check for the newly created one)`
  - [x] (2 Punkte) Test `edit the created assignee (200) and retrieve it with the change`
  - [x] (1 Punkt) Test `delete the created assignee (200)`
  - [x] (1 Punkt) Test `edit for non-existing assignee fails (404)`
  - [x] (1 Punkt) Test `delete for non-existing assignee fails (404)`
  - [x] (0,5 Punkte) Test `validation: assignee with empty prename fails (400)`
  - [x] (0,5 Punkte) Test `validation: assignee with empty name fails (400)`
  - [x] (0,5 Punkte) Test `validation: assignee with empty email fails (400)`
  - [x] (0,5 Punkte) Test `validation: assignee with valid non-uni email fails (400)`
  - [x] (0,5 Punkte) Test `validation: assignee with invalid uni email fails (400)`
- Suite `todos.tests.js` (12.5 / 12,5 Punkte)
  - [x] (1 Punkt) Test `create a valid todo (201, id returned)`
  - [x] (1 Punkt) Test `retrieve all todos (check for the newly created one)`
  - [x] (1 Punkt) Test `retrieve the created todo (check for attributes)`
  - [x] (1 Punkt) Test `delete an assignee (200) and check if the todo is updated`
  - [x] (1,5 Punkte) Test `edit the created todo (200) and retrieve it with the change`
  - [x] (0,5 Punkte) Test `validation: todo with empty title fails (400)`
  - [x] (0,5 Punkte) Test `validation: todo with invalid assigneeIds fails (400)`
  - [x] (0,5 Punkte) Test `validation: todo with 3x the same assigneeId fails (400)`
  - [x] (0,5 Punkte) Test `validation: todo with invalid dueDate fails (400)`
  - [x] (1 Punkt) Test `delete the created todo (200)`
  - [x] (1 Punkt) Test `edit for non-existing todo fails (404)`
  - [x] (1 Punkt) Test `delete for non-existing todo fails (404)`
  - [x] (2 Punkte) Test `CSV export for todos is valid (200, application/csv or text/csv)` <span style="color: red">(NEU)</span>
- ToDo-Kategorien (3 / 3 Punkte) <span style="color: red">(NEU)</span>
  - [x] (1,5 Punkte) Beim Erstellen eines `ToDos` wird eine durch das ML-Modell generierte Kategorie hinzugefügt.
  - [x] (1,5 Punkte) Beim Update eines `ToDos` wird die durch das ML-Modell generierte Kategorie aktualisiert.


## Persistenz über DB (3 / 3)

- [x] (1 Punkte) `Assignees` werden vollständig in einer DB-Tabelle persistiert
- [x] (1 Punkte) `ToDos` werden vollständig in einer DB-Tabelle persistiert
- [x] (1 Punkt) Mapping zwischen `Assignees` und `ToDos` wird vollständig in einer DB-Tabelle persistiert

## Funktionalität des Frontends (21 / 22)
> Anforderungen für maximale Punktzahl: Frontend hat alle geforderten Views / View-Elemente mit korrekter Funktionalität, vollständige Integration mit der API, Browser-Console (F12) enthält keine Fehler

- [x] (8 / 8 Punkte) View zur Verwaltung von `Assignees`
  - [x] (1 Punkt) View-Elemente für `GET /assignees`
  - [x] (1 Punkt) View-Elemente für `GET /assignees/$id`
  - [x] (2 Punkte) View-Elemente für `POST /assignees`
  - [x] (3 Punkte) View-Elemente für `PUT /assignees/$id`
  - [x] (1 Punkt) View-Elemente für `DELETE /assignees/$id`
- [x] (13 / 13 Punkte) View zur Verwaltung von `ToDos`
  - [x] (1 Punkt) View-Elemente für `GET /todos`
  - [x] (1 Punkt) View-Elemente für `GET /todos/$id`
  - [x] (2 Punkte) View-Elemente für `POST /todos`
  - [x] (2 Punkte) View-Elemente für `PUT /todos/$id`
  - [x] (1 Punkt) View-Elemente für `DELETE /todos/$id`
  - [x] (1 Punkt) Einfaches Abhaken von `ToDos` möglich
  - [x] (1 Punkt) Abgehakte `ToDos` sind getrennt von offenen
  - [x] (1 Punkt) `ToDos` sind filterbar nach `title`
  - [x] (1 Punkt) `ToDos` sind sortierbar nach `title` und `dueDate`
  - [x] (1 Punkt) View-Elemente für `GET /csv-downloads/todos` (CSV-Export und Speicherung der Datei) <span style="color: red">(NEU)</span>
  - [x] (1 Punkt) View-Elemente zur Anzeige der Kategorie bei `GET /todos` und `GET /todos/$id` <span style="color: red">(NEU)</span>
- [ ] (1 Punkt) Browser-Console (F12) zeigt keine JS Errors an // "Uncaught (in promise) TypeError: Failed to fetch"

## Usability (3 / 5) <span style="color: red">(NEU)</span>

- [x] (1 Punkt) Sinnvolle Systemrückmeldungen sind vorhanden (z.B. erfolgreiches Bearbeiten)
- [ ] (1 Punkt) Mögliche Anwenderfehler werden minimiert, konkrete Fehler werden kommuniziert (z.B. bei HTTP 400) // "Error: Validation failed for object='assignee'. Error count: 1"
- [ ] (1 Punkt) Gängige UI Components werden passend eingesetzt (z.B. Tables, MultiSelects, DatePicker, etc.) // Kein Date picker bei datum
- [x] (1 Punkt) UI ist verständlich und stellt notwendige Informationen effizient bereit (z.B. wenige Klicks notwendig, keine manuelle Eingabe von IDs)
- [x] (1 Punkt) Terminologie, Farben und Layout sind konsistent

## Design-Qualität (5 / 5) <span style="color: red">(NEU)</span>

- [x] (1 Punkt) Backend: Sinnvolle Package-Struktur (z.B. package by layer oder feature), Inhalt der Packages passt zusammen
- [x] (1 Punkt) Frontend: Sinnvolle Folder-Struktur (z.B. package by layer oder feature), Inhalt der Ordner passt zusammen
- [x] (1,5 Punkte) Backend: Sinnvolle Abstraktion und Auslagerung von Domain Logic (**nicht** alle Logik in Controllern, Nutzung der Spring Components), nicht zu große / komplexe `.java` Files
- [x] (1,5 Punkte) Frontend: Sinnvolle Abstraktion und Auslagerung von Application Logic (**nicht** alle Logik in `.vue` Files), nicht zu große / komplexe `.js` oder `.vue` Files
- [x] API: Korrekte HTTP Status Codes <sup>**1**</sup>
- [x] API: Rückgabe einer Message oder Domain Entity für `POST`, `PUT`, und `DELETE` <sup>**1**</sup>

<sup>**1**</sup> Dies wird bereits durch die Testfälle überprüft, daher gibt es hierfür keinen zusätzlichen Punktabzug.

## Code-Qualität (5 / 5) <span style="color: red">(NEU)</span>

- [x] (1 Punkt) Code ist konsistent formatiert
- [x] (1 Punkt) Code ist hinreichend kommentiert
- [x] (1 Punkt) Kein auskommentierter Code, keine unbenutzten Variablen, Funktionen oder Importe
- [x] (1 Punkt) Sinnvoll benannte Variablen und Funktionen
- [x] (1 Punkt) Nicht zu komplexe / lange Funktionen

## Repository und README (4,5 / 5)

- [x] (1 Punkt) Repository-Struktur folgt dem Beispielprojekt (`api` und `frontend` auf höchster Ebene)
- [x] (1 Punkt) Keine IDE-spezifischen Dateien (z.B. `.idea`, `.vscode`, etc.) oder Binaries / Libraries (z.B. `target`, `node_modules`, etc.) im Repository
- [x] (1 Punkt) Git tag `v0.4` vorhanden
- [x] (1 Punkt) DB- und API-Konfiguration entspricht dem Beispielprojekt (keine Änderungen für Korrektur nötig)
- [x] (0,5 Punkte) Keine Dummy-Dateien oder irrelevanter Text aus dem Beispiel-Projekt mehr vorhanden
- [ ] (0,5 Punkte) README enthält klare, IDE-unabhängige Anweisungen zur Ausführung der Applikation // Frontend starten fehlt


---

## Punkteschlüssel

Die folgende Tabelle verdeutlicht die Zuordnung von Punkten zu Noten:

| Punkte      | Note |
| ----------- | ---- |
| < 35,0      | 5,0  |
| 35,0 - 38,5 | 4,0  |
| 39,0 - 42,0 | 3,7  |
| 42,5 - 45,5 | 3,3  |
| 46,0 - 49,0 | 3,0  |
| 49,5 - 52,5 | 2,7  |
| 53,0 - 56,0 | 2,3  |
| 56,5 - 59,5 | 2,0  |
| 60,0 - 63,0 | 1,7  |
| 63,5 - 66,5 | 1,3  |
| 67,0 - 70,0 | 1,0  |
