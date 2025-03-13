# Bewertung des 2. Arbeitsblatts

> Das Arbeitsblatt besteht aus 30 Punkten, von denen 15 zum Bestehen notwendig sind.

## Übersicht der erreichten Punkte

| Kriterium                     | erreichte Punkte / maximale Punkte |
| ----------------------------- |------------------------------------|
| Funktionalität der API        | 17,5 / 20                          |
| Persistenz über DB            | 5 / 5                              |
| Repository, Config und README | 4 / 5                              |
| **Summe**:                    | 26,5 / 30                          |
| **Bestanden**:                | ja                                 |

## Funktionalität der API (17,5 / 20)
> Anforderungen für maximale Punktzahl: API hat alle geforderten Ressourcen (URLs plus Request und Response Bodies) mit korrekter Funktionalität, d.h., alle [automatisierten API-Tests](https://github.tik.uni-stuttgart.de/ISTE-ESE/pe2-api-tests-ex2) wurden bestanden.
> Pro bestandenem Testfall gibt es eine bestimmte Menge an Punkten (siehe unten).

### Ergebnisse der automatisierten API-Tests

| Suite                 | # of Tests | # of Passed | # of Failed |
| --------------------- | ---------- |-------------|-------------|
| `assignees.tests.js`: | 11         | 11          | 0           |
| `todos.tests.js`:     | 12         | 10          | 2           |


### Bewertung

Detaillierte Auswertung für alle automatisierten API-Tests:

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
- Suite `todos.tests.js` (8 / 10,5 Punkte)
  - [x] (1 Punkt) Test `create a valid todo (201, id returned)`
  - [x] (1 Punkt) Test `retrieve all todos (check for the newly created one)`
  - [x] (1 Punkt) Test `retrieve the created todo (check for attributes)`
  - [ ] (1 Punkt) Test `delete an assignee (200) and check if the todo is updated`
  - [ ] (1,5 Punkte) Test `edit the created todo (200) and retrieve it with the change`
  - [x] (0,5 Punkte) Test `validation: todo with empty title fails (400)`
  - [x] (0,5 Punkte) Test `validation: todo with invalid assigneeIds fails (400)`
  - [x] (0,5 Punkte) Test `validation: todo with 3x the same assigneeId fails (400)`
  - [x] (0,5 Punkte) Test `validation: todo with invalid dueDate fails (400)`
  - [x] (1 Punkt) Test `delete the created todo (200)`
  - [x] (1 Punkt) Test `edit for non-existing todo fails (404)`
  - [x] (1 Punkt) Test `delete for non-existing todo fails (404)`

## Persistenz über DB (5 / 5)

- [x] (2 Punkte) `assignees` werden vollständig in einer DB-Tabelle persistiert
- [x] (2 Punkte) `todos` werden vollständig in einer DB-Tabelle persistiert
- [x] (1 Punkt) Mapping zwischen `assignees` und `todos` wird vollständig in einer DB-Tabelle persistiert

## Repository, Config und README (4 / 5)

- [x] (1 Punkt) Repository-Struktur folgt dem Beispielprojekt (Folder `api` auf höchster Ebene)
- [x] (1 Punkt) Keine IDE-spezifischen Dateien (z.B. `.idea`, `.vscode`, etc.) oder Binaries / Libraries (z.B. `target`, `node_modules`, etc.) im Repository
- [x] (1 Punkt) Git tag `v0.2` vorhanden
- [ ] (1 Punkt) DB- und API-Konfiguration entspricht dem Beispielprojekt (keine Änderungen für Korrektur nötig) 
  - Datenbank Url entspricht nicht dem Beispielprojekt 
- [x] (0,5 Punkte) Keine Dummy-Dateien oder irrelevanter Text aus dem Beispiel-Projekt mehr in `api`
- [x] (0,5 Punkte) README enthält klare, IDE-unabhängige Anweisungen zur Ausführung der Applikation

