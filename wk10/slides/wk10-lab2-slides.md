
---
marp: true
title: Week 10 • Lab 2 — Inclusive Redesign & Packaging
paginate: true
---

# Week 10 • Lab 2
### Implement redesign · Re-verify accessibility · Package Task 2

- Ship the prioritised inclusive change
- Re-run parity + SR checks
- Collect post-change metrics
- Assemble the Task 2 bundle

---

## Learning outcomes
- Implement redesign with server-first + HTMX parity
- Complete accessibility regression checklist
- Compare before/after metrics
- Prepare Task 2 submission artefacts

---

## Recap: redesign brief
- Problem backed by metrics + notes
- Inclusion impact articulated
- Acceptance tests defined (keyboard, SR, JS-off)
- Measurement plan (which metrics to re-run)

---

## Implementation tips
- Work in focused diffs (`feat(a11y): …`)
- Keep partials aligned to avoid duplication
- Update instrumentation if events change
- Log validation errors + successes consistently

---

## Worked example
### Inclusive validation redesign
- Add alert banner (`role="alert"`) for JS-off path
- Link banner to input via `aria-describedby`
- Maintain HTMX live region updates
- Evidence: completion ↑, error ↓, regression checklist PASS

---

## Accessibility regression checklist
`wk10/lab-wk10/a11y/regression-checklist.csv`
- Keyboard flows
- Focus order + visibility
- Error messaging (`aria-describedby`)
- Dynamic updates (`role="status"`)
- No-JS parity
- Contrast / zoom (WCAG AA)
Record Pass/Notes; backlog any failures.

---

## Post-change metrics
1. New session IDs for re-test
2. Run affected tasks (JS on/off)
3. Append to `data/metrics.csv`
4. Re-run analysis → `analysis/postchange.csv`
5. Update before/after table in `03-before-after-summary.md`

---

## Task 2 bundle contents
- `01-redesign-brief.md`
- `02-a11y-regression-checklist.csv`
- `03-before-after-summary.md`
- `04-key-diffs.md`
- `05-evidence/` (annotated screenshots, transcripts)
- `06-metrics/pre|post` (analysis CSVs)
Ensure filenames + structure match spec.

---

## Reflection & backlog updates
- Mark implemented backlog item done (reference commit)
- Add new items for uncovered risks
- Update `research/notes.md` with impacts + remaining concerns

---

## Deliverables today
- Code changes merged for inclusive redesign
- Regression checklist completed
- Post-change metrics and summaries stored
- Task 2 bundle ready for submission

---

## Next steps
- Week 11: present evidence at crit
- Finalise wrap-up + portfolio in Lab 2
- Keep artefacts organised for assessors

