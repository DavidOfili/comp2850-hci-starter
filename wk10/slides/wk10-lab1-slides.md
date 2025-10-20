
---
marp: true
title: Week 10 • Lab 1 — Analyse & Prioritise
paginate: true
---

# Week 10 • Lab 1
### Analyse metrics · Interpret inclusion impacts · Plan redesign

- Turn Week 9 data into insight
- Compare JS-on vs JS-off performance
- Score backlog items for impact
- Draft an inclusive redesign brief

---

## Why analysis matters
- Demonstrates evidence-based decision-making
- Highlights inclusion gaps (parity issues, cognitive load)
- Sets up Task 2 narrative (“we measured, we improved”)
- References:
  - `sources/evaluation-metrics-quickref.md`
  - Week 9 pilot artefacts (`data/metrics.csv`, notes)

---

## Learning outcomes
- Calculate medians, MAD, completion/error rates
- Interpret metrics with inclusion lenses
- Prioritise backlog items transparently
- Capture plan in `redesign-brief.md`

---

## Analysis pipeline
1. Run `Analyse.kt` → `analysis/analysis.csv`
2. Summarise in `analysis/summary.md`
3. Interpret inclusion impacts (narratives)
4. Prioritise fixes → `analysis/prioritisation.csv`
5. Draft redesign brief referencing evidence

---

## Metrics refresher
- **Median time**: typical effort, resistant to outliers
- **MAD**: variation; high values hint at inconsistent experiences
- **Completion rate**: success / attempts; <1.0 signals blockers
- **Error rate**: validation issues; watch JS-off split
- **Subjective metrics**: UMUX-Lite, confidence (include in narrative)

---

## Worked example
`analysis/analysis.csv` excerpt:
```
Task,T2_edit js_off
Median 2300 ms, MAD 450 ms
Completion 0.50, Error 0.50
```
Interpretation → backlog:
- JS-off parity broken (only half complete)
- Add backlog row referencing metrics + notes
- Candidate fix: page-level alert + focus link

---

## Building summary tables
- Table 1: `Median/MAD` by task + `js_mode`
- Table 2: `Completion/Error` by task
- Add narrative paragraphs per task (3–4 sentences)
- Link to evidence (metrics rows, pilot notes)

---

## Prioritisation scoring
`score = (impact + inclusion) - effort`
- Impact: metrics severity (completion hit? error rate? time cost?)
- Inclusion: breadth/depth of affected groups
- Effort: engineering/UX complexity (lower = easier)
- Record in `analysis/prioritisation.csv` with evidence links

---

## Redesign brief structure
```
Problem → Goal → Inclusion impact → Changes
→ Acceptance tests → Measurement plan
```
- Cite metrics + backlog IDs
- Define measurable targets (e.g., completion ≥0.9)
- Keep to one page for clarity

---

## Deliverables today
- `analysis/analysis.csv`
- `analysis/summary.md` with tables + narratives
- `analysis/prioritisation.csv` (scored backlog)
- `wk10/lab-wk10/docs/redesign-brief.md`
- Update `03-before-after-summary.md` with baseline metrics

---

## Next lab
- Implement inclusive redesign
- Re-run metrics + accessibility checks
- Package Task 2 evidence

