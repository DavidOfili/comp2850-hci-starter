# Regression Testing Checklist

**Module**: COMP2850 HCI
**Week**: 10 Lab 2
**Student**: [Your Name]
**Date**: [YYYY-MM-DD]
**Fixes Tested**: [List commit hashes]

---

## Purpose

After implementing priority fixes, verify that:
1. Fixes work as intended (new behaviour correct)
2. Existing features still work (no regressions)
3. All interaction modes supported (HTMX, no-JS, keyboard, SR)

---

## Test Matrix

| Test | Path | Tool | Result | Notes |
|------|------|------|--------|-------|
| **T1: Filter Tasks** ||||
| Filter applies (HTMX) | HTMX, mouse | Chrome | ☐ Pass ☐ Fail | |
| Filter applies (no-JS) | No-JS, mouse | Chrome | ☐ Pass ☐ Fail | |
| Filter keyboard-accessible | HTMX, keyboard | Firefox | ☐ Pass ☐ Fail | Tab to dropdown, Space/Enter selects |
| Filter SR-announced | HTMX, SR | Firefox + NVDA | ☐ Pass ☐ Fail | "Filter applied, showing 3 items" or similar |
| **T2: Edit Task** ||||
| Edit activates (HTMX) | HTMX, mouse | Chrome | ☐ Pass ☐ Fail | Click "Edit", form appears |
| Edit activates (keyboard) | HTMX, keyboard | Firefox | ☐ Pass ☐ Fail | Tab to "Edit", Enter activates |
| Edit saves (HTMX) | HTMX, mouse | Chrome | ☐ Pass ☐ Fail | New title shown after save |
| Edit saves (no-JS) | No-JS, mouse | Chrome | ☐ Pass ☐ Fail | Form submits, PRG redirect, title updated |
| Edit focus management | HTMX, keyboard | Firefox | ☐ Pass ☐ Fail | Focus moves to input; focus visible |
| Edit SR-announced | HTMX, SR | Firefox + NVDA | ☐ Pass ☐ Fail | "Title, edit, [existing value]" announced |
| **T3: Add Task** ||||
| Add succeeds (HTMX) | HTMX, mouse | Chrome | ☐ Pass ☐ Fail | Task added, OOB status update |
| Add succeeds (no-JS) | No-JS, mouse | Chrome | ☐ Pass ☐ Fail | PRG redirect, new task in list |
| Validation error (HTMX) | HTMX, mouse | Chrome | ☐ Pass ☐ Fail | Blank title → error div with role="alert" |
| Validation error SR | HTMX, SR | Firefox + NVDA | ☐ Pass ☐ Fail | **[NEW FIX]** NVDA announces error immediately |
| Validation error (no-JS) | No-JS, mouse | Chrome | ☐ Pass ☐ Fail | **[NEW FIX]** Redirect to error summary, focus on heading |
| **T4: Delete Task** ||||
| Delete succeeds (HTMX) | HTMX, mouse | Chrome | ☐ Pass ☐ Fail | OOB swap removes `<li>`, status updated |
| Delete succeeds (no-JS) | No-JS, mouse | Chrome | ☐ Pass ☐ Fail | PRG redirect, task removed |
| Delete keyboard-accessible | HTMX, keyboard | Firefox | ☐ Pass ☐ Fail | Tab to "Delete", Enter activates |
| Delete SR-announced | HTMX, SR | Firefox + NVDA | ☐ Pass ☐ Fail | "Task deleted" or similar confirmation |
| **Focus Visible (NEW FIX)** ||||
| Focus outline on buttons | HTMX, keyboard | Firefox | ☐ Pass ☐ Fail | **[NEW FIX]** 3px solid #1976d2 outline visible |
| Focus outline on inputs | HTMX, keyboard | Firefox | ☐ Pass ☐ Fail | Custom outline applies to text inputs |
| Focus outline on links | HTMX, keyboard | Firefox | ☐ Pass ☐ Fail | Custom outline applies to all interactive elements |
| **No Regressions** ||||
| All tasks complete (HTMX) | HTMX, mouse | Chrome | ☐ Pass ☐ Fail | T1, T2, T3, T4 all work end-to-end |
| All tasks complete (no-JS) | No-JS, mouse | Chrome | ☐ Pass ☐ Fail | T1, T2, T3, T4 all work without JS |
| No console errors | HTMX, mouse | Chrome DevTools | ☐ Pass ☐ Fail | Open Console, check for red errors |
| No axe violations | HTMX, mouse | Chrome + axe DevTools | ☐ Pass ☐ Fail | Run axe scan, 0 violations (or document known issues) |

---

## Test Execution Notes

### Environment
- **Browser(s)**: [Chrome 120, Firefox 121, Safari 17]
- **Screen Reader**: [NVDA 2024.1 / VoiceOver / JAWS]
- **OS**: [Windows 11 / macOS 14 / Linux]
- **Application URL**: [http://localhost:8080/tasks]

### Failures Detected
[If any tests fail, document here:]

**Example**:
> **Test**: T3 validation error SR (HTMX, NVDA)
> **Result**: Fail
> **Issue**: Error div added `role="alert"` but NVDA did not announce. Investigation: missing `aria-live="assertive"`.
> **Fix applied**: Commit abc123f - added `aria-live="assertive"` to error div.
> **Retest**: Pass (NVDA now announces "Title is required" immediately)

### Re-tests Required
- [ ] [Test name] - Reason: [Fix applied, needs verification]

---

## Sign-Off

**All tests passed**: ☐ Yes ☐ No (see failures above)
**Ready for Task 2 submission**: ☐ Yes ☐ No
**Date completed**: [YYYY-MM-DD]
**Tester**: [Your Name]

---

**Template source**: Week 10 Lab Pack, COMP2850 (University of Leeds)
**Reference**: Week 10 Lab 2 (Activity 3)
