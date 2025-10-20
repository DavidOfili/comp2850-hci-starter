# Week 10 Lab Pack: Analysis & Inclusive Redesign (Consolidated)

**Purpose**: Complete lab pack for Week 10 analysis and redesign activities (Labs 1 & 2)
**Status**: Consolidated from `wk10/lab-wk10/` and `wk10/lab-w10/`
**Date**: 2025-10-14

---

## Directory Structure

```
consolidated/lab-wk10/
├── README.md                        # This file
├── a11y/                            # Accessibility verification
│   └── regression-checklist.csv    # Post-redesign a11y checks
├── docs/                            # Redesign documentation
│   └── redesign-brief.md           # Problem statement & goals template
├── scripts/                         # Analysis automation
│   └── Analyse.kt                  # Median, MAD, completion rate calculator
└── templates/                       # Student starter templates
    ├── prioritisation-matrix.md    # (Impact + Inclusion) - Effort scoring
    └── regression-checklist.md     # A11y verification matrix (markdown)
```

---

## Contents Overview

### Analysis Scripts (`scripts/`)

**Analyse.kt**
- Kotlin script for automated metrics analysis
- Reads: `data/metrics.csv` (from Week 9 pilots)
- Outputs: `analysis.csv` with per-task statistics

**Metrics calculated**:
- `median_ms` - Median time-on-task (robust to outliers)
- `mad_ms` - Median Absolute Deviation (variability measure)
- `completion_rate` - Percentage of successful attempts
- `error_rate` - Validation/server errors per attempt
- `n_success` - Count of successful completions

**Usage**:
```bash
cd consolidated/lab-wk10/scripts/
kotlin Analyse.kt ../../data/metrics.csv
# Outputs: analysis.csv
```

**Formula references**:
- Median: Middle value when sorted (50th percentile)
- MAD: `median(|x_i - median(x)|)`
- Completion rate: `success / (success + fail + error)`

### Prioritisation (`templates/prioritisation-matrix.md`)

**Scoring framework**: `Priority = (Impact + Inclusion) - Effort`

**Scales** (1-5 for each dimension):

**Impact** (functional severity):
- 5: Blocks task completion
- 4: Major slowdown
- 3: Moderate inconvenience
- 2: Minor annoyance
- 1: Cosmetic

**Inclusion** (accessibility/equity):
- 5: Excludes entire user group (e.g., screen reader users)
- 4: Significant barrier (e.g., keyboard trap)
- 3: Moderate barrier (e.g., missing label)
- 2: Minor improvement (e.g., better contrast)
- 1: Nice-to-have

**Effort** (implementation cost):
- 5: >4 hours, complex changes
- 4: 2-4 hours, multiple files
- 3: 1-2 hours, single feature
- 2: 30min-1hr, config change
- 1: <30min, quick fix

**Example**:
```
Issue: Filter validation error not announced to screen readers
Impact: 3 (moderate delay)
Inclusion: 5 (excludes SR users)
Effort: 2 (add aria-live region)
Priority: (3+5)-2 = 6 (HIGH)
```

### Redesign Documentation (`docs/redesign-brief.md`)

**Template structure**:
1. **Problem Statement**
   - Evidence from pilots (median times, error rates, quotes)
   - Specific personas affected (SR users, keyboard-only, low-vision)

2. **Proposed Solution**
   - Design sketch/wireframe
   - WCAG criteria addressed
   - No-JS parity plan

3. **Success Metrics**
   - Target median time reduction (e.g., T2: 45s → 30s)
   - Error rate reduction (e.g., 40% → <10%)
   - Confidence improvement (e.g., 2.8 → 4.0)

4. **Implementation Plan**
   - Files to change
   - Template updates
   - Route modifications

### Accessibility Regression Testing (`a11y/`)

**regression-checklist.csv**
- Verification matrix for post-redesign testing
- Ensures accessibility not broken by changes

**Columns**:
- `criterion` - WCAG 2.2 AA criteria (e.g., 1.3.1, 2.1.1)
- `check` - Specific verification (e.g., "Form labels programmatically associated")
- `htmx_mode` - Pass/Fail for HTMX-enhanced mode
- `nojs_mode` - Pass/Fail for traditional form submission
- `notes` - Details, evidence, screenshots

**regression-checklist.md** (templates/)
- Markdown version for easier editing
- Same structure, human-readable format

**Key checks**:
- Keyboard navigation (no traps, logical order)
- Screen reader announcements (ARIA live regions)
- Form validation (error messages linked)
- Focus management (after dynamic updates)
- No-JS parity (feature equivalence)

---

## Usage for Students

### Week 10 Lab 1: Analyse & Prioritise

1. **Run metrics analysis**:
   ```bash
   cd consolidated/lab-wk10/scripts/
   kotlin Analyse.kt ../../data/metrics.csv > analysis.csv
   ```

2. **Review results**:
   ```csv
   task_code,js_mode,n_success,median_ms,mad_ms,errors_validation,completion_rate,error_rate
   T1_filter,htmx,4,12500,2100,1,0.80,0.20
   T1_filter,nojs,4,15300,3200,2,0.67,0.33
   ```

3. **Identify issues from pilot notes**:
   - Check `wk09/lab-wk9/research/pilots/` for qualitative findings
   - Link metrics to specific problems (e.g., high T2 median → confusing inline edit)

4. **Score with prioritisation matrix**:
   ```markdown
   | Issue | Impact | Inclusion | Effort | Priority |
   |-------|--------|-----------|--------|----------|
   | Filter SR announcement | 3 | 5 | 2 | 6 |
   | Edit button contrast | 2 | 3 | 1 | 4 |
   ```

5. **Select top 3-5 issues** for redesign (highest priority scores)

6. **Write redesign brief** (use `docs/redesign-brief.md` template):
   - Problem: Evidence-based (metrics + quotes)
   - Solution: Sketch + WCAG mapping
   - Goals: Measurable improvements

### Week 10 Lab 2: Implement & Verify

1. **Implement redesign** (code changes)
   - Update templates (Pebble files)
   - Modify routes (Kotlin)
   - Test dual-mode (HTMX + no-JS)

2. **Re-verify accessibility**:
   ```bash
   # Use regression checklist
   cp consolidated/lab-wk10/templates/regression-checklist.md a11y/

   # Test systematically:
   # - Keyboard-only navigation
   # - Screen reader (NVDA/VoiceOver)
   # - HTMX vs no-JS parity
   # - axe DevTools scan
   ```

3. **Collect post-redesign metrics** (optional):
   - Run 2-3 quick pilots
   - Compare to pre-redesign (Week 9 data)

4. **Package Task 2 submission**:
   ```
   wk10/gradescope/task2/
   ├── 01-redesign-brief.md
   ├── 02-a11y-regression-checklist.csv
   ├── 03-before-after-summary.md
   ├── 04-key-diffs.md
   ├── 05-evidence/
   └── 06-metrics/
       ├── pre/analysis.csv
       └── post/postchange.csv
   ```

---

## Analysis Output Example

**Input** (`data/metrics.csv`):
```csv
ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode
2025-10-15T14:32:10Z,P1_7a9f,r001,T1_filter,success,ok,12500,200,htmx
2025-10-15T14:35:22Z,P2_3c8d,r002,T1_filter,validation_error,blank_query,8200,422,nojs
```

**Output** (`analysis.csv`):
```csv
task_code,js_mode,n_success,median_ms,mad_ms,errors_validation,completion_rate,error_rate
T1_filter,htmx,4,12500,2100,1,0.80,0.20
T1_filter,nojs,3,15300,3200,2,0.60,0.33
T2_edit,htmx,5,8900,1500,0,1.00,0.00
T2_edit,nojs,4,11200,2300,1,0.80,0.20
```

**Interpretation**:
- T1 (filter): Higher error rate in no-JS mode → validation not clear
- T1 median: 22% slower without HTMX → acceptable tradeoff
- T2 (edit): Perfect completion in HTMX, some issues in no-JS → focus on parity

---

## Privacy & Evidence Collection

### Metrics Storage
- **Pre-redesign**: `data/metrics.csv` (from Week 9)
- **Post-redesign**: `data/metrics-postchange.csv` (Week 10 Lab 2)
- **Analysis outputs**: `wk10/gradescope/task2/06-metrics/`

### Screenshots
- **Crop personal info**: No usernames, emails, photos
- **Annotate**: Highlight changed areas with arrows/boxes
- **Before/after pairs**: Same task, same viewport size
- **Store**: `wk10/gradescope/task2/05-evidence/screenshots/`

---

## References

### Lab Documentation
- Week 10 Lab 1: `wk10/src/wk10-lab1-analysis-prioritisation.md`
- Week 10 Lab 2: `wk10/src/wk10-lab2-redesign-reverify-package.md`

### Supporting Guides
- `sources/evaluation-metrics-quickref.md` - Median/MAD formulas
- `sources/assistive-testing-checklist.md` - Step-by-step a11y testing
- `wk09/lab-wk9/` - Week 9 pilot data and protocols

### Statistical Methods
- Median: [Wikipedia: Median](https://en.wikipedia.org/wiki/Median)
- MAD: [Wikipedia: Median Absolute Deviation](https://en.wikipedia.org/wiki/Median_absolute_deviation)
- Why not mean? Medians robust to outliers (one slow pilot won't skew results)

### WCAG References
- [WCAG 2.2 Quick Reference](https://www.w3.org/WAI/WCAG22/quickref/)
- Key criteria: 1.3.1 (Info & Relationships), 2.1.1 (Keyboard), 4.1.3 (Status Messages)

---

## Consolidation Notes

**Merged from**:
- `wk10/lab-wk10/` (older structure: a11y/, docs/, scripts/)
- `wk10/lab-w10/` (newer templates/)

**Changes**:
- Combined all content into single directory
- Added comprehensive README
- Clarified template vs. working file distinction (regression-checklist.md vs .csv)

**Original directories preserved** - this is a consolidated copy for clarity.

---

## Task 2 Submission Checklist

Before submitting to Gradescope:

- [ ] Redesign brief includes evidence (metrics, quotes)
- [ ] Prioritisation matrix shows scoring for ≥5 issues
- [ ] Accessibility regression checklist complete (HTMX + no-JS)
- [ ] Before/after metrics comparison included
- [ ] Key code diffs documented
- [ ] Screenshots cropped (no PII)
- [ ] All files in correct `wk10/gradescope/task2/` structure
- [ ] Git history clean (no secrets committed)

---

**Created**: 2025-10-14
**Module**: COMP2850 HCI
**Contact**: See module staff on Minerva
