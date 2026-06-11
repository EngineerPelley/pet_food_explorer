# Git

**What it is:** Version control — records snapshots ("commits") of the project
over time so you can see history, undo mistakes, and collaborate.

**David's level: introduced**

## What David knows

*(from the 2026-06-11 session)*

- Committing is a two-step process: **staging** (`git add <file>` — choosing
  which changes to include) then **committing** (`git commit -m "message"` —
  recording the snapshot).
- Commits are **local** — they live on this machine. Sharing them with a
  server like GitHub is a separate step (`git push`).
- The "LF will be replaced by CRLF" warning is harmless: Windows and
  Linux/Mac use different invisible end-of-line characters and git converts
  automatically.

## Next things to learn

- `git status`, `git log`, `git diff` — seeing what changed.
- Branches — working on a feature without touching `main`.
- `.gitignore` — why `node_modules/` and build output are not committed.
- Undoing things safely (`git restore`, `git revert`).

## Notes

(none yet)
