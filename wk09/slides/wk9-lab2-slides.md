
---
marp: true
title: Week 9 • Lab 2 — Pilots, Debrief, Task 1 Draft
paginate: true
---

# Week 9 • Lab 2
### Run pilots · Log evidence · Draft Task 1 pack

- Execute peer pilots per protocol
- Capture metrics + qualitative notes
- Update backlog with evidence
- Assemble Task 1 draft bundle

---

## Learning outcomes
- Facilitate ethical, consistent pilot sessions
- Validate instrumentation (JS on/off)
- Translate observations into backlog items
- Organise evidence for assessment

---

## Pilot logistics
- Triads: facilitator, participant, note-taker
- New session ID per participant (cookie or prompt)
- Reiterate consent script (opt-out at any time)
- Tools ready: metrics CSV, notes template, timer, SR toggle

---

## During the pilot
- Read tasks verbatim, minimal prompting
- Observe accessibility behaviours (hesitations, confirmations)
- Record subjective metrics (UMUX-Lite, confidence)
- Log anomalies immediately (notes + backlog idea)

---

## After each pilot
1. Check `data/metrics.csv` for new rows
2. Verify `js_mode`, `task_code`, `step` values make sense
3. If logs missing, rerun task manually and fix instrumentation
4. Swap roles and repeat

---

## Debrief → backlog
- Use severity × inclusion risk tags
- Reference evidence links (`data/metrics.csv#sid`, `pilot-notes.md#L…`)
- Mark candidate fixes for Week 10
- Example entry:
```
21,Filter SR feedback missing,T1_filter,"Participant B: no announcement",4,5,2,
  "wk09/lab-wk9/research/pilot-notes.md#L8","Add aria-described by result count",true
```

---

## Task 1 draft bundle
`wk09/lab-wk9/submission/task1-draft/`
- `01-eval-plan.md`
- `02-protocol.md`
- `03-results.csv` (copy of metrics slice)
- `04-findings.md` (top issues, evidence)
- `05-evidence/` (screenshots, transcripts, annotations)
Ensure no PII; add alt text to images.

---

## Sanity check metrics
- Run quick checks (median, error counts, completion)
- Spot anomalous durations or missing session IDs
- Update `analysis/summary.md` with preliminary observations if desired

---

## Reflection & notes
- Update `research/notes.md` with surprises + ethical notes
- Record unresolved risks for Week 10 prioritisation
- Backlog: mark items done or carry forward

---

## Deliverables today
- Two pilot runs logged (JS on/off coverage)
- Backlog entries populated with evidence links
- Task 1 draft folder populated
- Metrics sanity-checked

---

## Next steps
- Week 10: analyse metrics, prioritise fixes, draft redesign brief
- Keep raw data organised; you will reuse it multiple times

