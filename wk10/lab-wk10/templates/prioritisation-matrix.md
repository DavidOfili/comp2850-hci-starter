# Prioritisation Matrix Template

**Module**: COMP2850 HCI
**Week**: 10 Lab 1
**Student**: [Your Name]
**Date**: [YYYY-MM-DD]

---

## Scoring Framework

**Formula**: `Priority = (Impact + Inclusion) – Effort`

### Impact (1–5)
- **5**: Blocks task completion entirely
- **4**: Significant delay or high error rate
- **3**: Noticeable friction but workaround exists
- **2**: Minor annoyance
- **1**: Cosmetic issue

### Inclusion (1–5)
- **5**: Disproportionately affects people using assistive tech (WCAG failure)
- **4**: Affects keyboard-only or no-JS users
- **3**: Affects specific browser/device combinations
- **2**: Affects all users equally
- **1**: Affects minority use case

### Effort (1–5)
- **5**: Requires architectural refactor (8+ hours)
- **4**: Requires significant code changes (4–8 hours)
- **3**: Moderate changes across multiple files (2–4 hours)
- **2**: Simple fix in 1–2 files (30min–2 hours)
- **1**: Trivial fix (< 30 minutes)

---

## Issue Matrix

| ID | Issue Description | Evidence (Pilot, Task, Metric) | Impact | Inclusion | Effort | **Priority** |
|----|-------------------|-------------------------------|--------|-----------|--------|--------------|
| 1  | Validation errors not announced by SR | P3, T3, 127s (10.9 MAD outlier) | 5 | 5 | 2 | **8** |
| 2  | Focus outline invisible on buttons | P2, T2, "couldn't see which button had focus" | 3 | 4 | 1 | **6** |
| 3  | Error summary missing for no-JS | P4, T3, 45s (2.7 MAD above median) | 4 | 4 | 2 | **6** |
| 4  | Delete button has no confirmation | P1, T4, "worried I'd delete wrong task" | 2 | 2 | 3 | **1** |
| 5  | Add form doesn't auto-focus | P1, P2, T3, "had to click into input" | 2 | 2 | 1 | **3** |
| 6  | Filter dropdown label unclear | P2, T1, "not sure what 'Status' meant" | 2 | 2 | 1 | **3** |
| 7  | Success messages not persistent | P3, T3, "didn't hear confirmation" | 3 | 4 | 1 | **6** |
| 8  | Edit button positioned inconsistently | P1, P2, "had to search for Edit each time" | 2 | 2 | 2 | **2** |

---

## Priority Ranking (High to Low)

1. **Priority 8**: Issue #1 (Validation errors not announced by SR)
2. **Priority 6**: Issue #2 (Focus outline invisible), Issue #3 (Error summary missing), Issue #7 (Success messages not persistent)
3. **Priority 3**: Issue #5 (Add form auto-focus), Issue #6 (Filter label unclear)
4. **Priority 2**: Issue #8 (Edit button positioning)
5. **Priority 1**: Issue #4 (Delete confirmation)

---

## Redesign Focus (Week 10 Lab 2)

**Top 3 issues** (Priority 6+):
1. Issue #1: Add `role="alert" aria-live="assertive"` to error div
2. Issue #2: Add custom focus outline (3px solid #1976d2)
3. Issue #3: Add error summary component for no-JS path

**Deferred to backlog** (Priority 3 or below):
- Issue #4, #5, #6, #8

**Rationale**: Top 3 issues have highest inclusion impact (affect SR/keyboard/no-JS users) and low effort (1–2 hours each). Combined, they address all WCAG 4.1.3, 2.4.7, and 3.3.1 failures identified in Week 7 audit.

---

## Evidence Summary

### Issue #1: Validation Errors Not Announced
- **Pilot P3** (NVDA, keyboard, JS-on):
  - Task T3 (add task): 127 seconds
  - Median for other pilots: 18 seconds
  - MAD: 10 seconds → P3 is 10.9 MAD outlier
  - Pilot notes (line 14): "P3 submitted blank form three times; NVDA did not announce 'Title is required' error."
- **WCAG**: 4.1.3 Status Messages (AA) failure
- **Impact**: SR users cannot complete task without sighted assistance

### Issue #2: Focus Outline Invisible
- **Pilot P2** (keyboard-only, JS-on):
  - Task T2 (edit task): 28 seconds (median = 15s)
  - Pilot notes (line 22): "P2 said 'I can't tell which button is focused' after pressing Tab three times."
- **WCAG**: 2.4.7 Focus Visible (AA) failure
- **Impact**: Keyboard users lose track of navigation position

### Issue #3: Error Summary Missing (No-JS)
- **Pilot P4** (no-JS, mouse):
  - Task T3 (add task): 45 seconds (median = 18s, MAD = 10s → 2.7 MAD above)
  - Pilot notes (line 8): "P4 missed error text at top of form; re-scanned page for 15 seconds."
- **WCAG**: 3.3.1 Error Identification (A) failure (no-JS path)
- **Impact**: No-JS users take 2.5× longer to identify and fix errors

---

**Template source**: Week 10 Lab Pack, COMP2850 (University of Leeds)
**Reference**: Week 10 Lab 1 (Activity 2), `sources/evaluation-metrics-quickref.md`
