# Contributing to ENG1-Group-Project

This doc explains how our team uses Git and GitHub, so everyone works the same way,
nobody overwrites anyone else's work, and `main` always stays in a working state.

If you prefer a visual tool instead of the command line, [GitHub Desktop](https://desktop.github.com)
does the same steps through buttons. The rules below still apply either way.

## Branches in this repo

- **`main`** — stable and always working. This is what gets submitted/deployed.
  Only ever updated by merging `dev` into it, once the team agrees `dev` is in a
  good state.
- **`dev`** — the shared working branch. Everyone's finished work lands here first,
  and this is where we test things together before they reach `main`.
- **`feature/...`, `fix/...`, `docs/...`** — short-lived personal branches, branched
  off `dev` (not `main`), for your own work in progress.

## The core rules

1. **Never commit directly to `main`** (unless you are adding a menial file like a
   `.md` or `.txt` doc). `main` only changes when `dev` is merged into it.
2. **Branch off `dev` for your own work**, not `main`. Merge your branch back into
   `dev` yourself once it's ready — no formal review needed for this step.
3. **Pull the latest `dev` before you start new work**, so your branch starts from
   the current state.
4. **Only merge `dev` into `main` once the team has actually tested on `dev` and
   agrees it's working.** Say so in the team chat before doing it.
5. **Write clear commit messages** — what changed and why, not "fix" or "update".
6. **Never commit secrets, credentials, or large generated files** (see "What not
   to commit" below).

## Branch naming

Branch off `dev` using this pattern:

```
<type>/<short-description>
```

- `feature/risk-register-table`
- `fix/broken-meeting-log-link`
- `docs/update-requirements-doc`

Use `feature/`, `fix/`, or `docs/` as the prefix, and keep the description short,
lowercase, and hyphenated.

## Repo setup (do this once, as a team)

The `dev` branch doesn't exist yet. Whoever does this first should create it from
`main` and push it, so everyone else has something to branch off:

```bash
git checkout main
git pull
git checkout -b dev
git push -u origin dev
```

## Step-by-step workflow

### 1. One-time setup (each new computer)

```bash
git clone https://github.com/RemekUni/ENG1-Group-Project.git
cd ENG1-Group-Project
```

### 2. Start new work

Before branching, make sure your local `dev` is up to date:

```bash
git checkout dev
git pull
```

Then create your branch:

```bash
git checkout -b feature/your-branch-name
```

### 3. Make your changes

Edit files as normal. When you're ready to save a checkpoint:

```bash
git add <files-you-changed>
git commit -m "Add short description of what changed"
```

- Prefer `git add <specific files>` over `git add .` — it stops you accidentally
  committing files you didn't mean to.
- Commit in small, logical chunks rather than one giant commit at the end.

### 4. Push your branch

```bash
git push -u origin feature/your-branch-name
```

This backs your work up on GitHub and lets others see it, even though you'll merge
it yourself without a PR.

### 5. Merge your branch into dev

Once your change is working:

```bash
git checkout dev
git pull
git merge feature/your-branch-name
git push
```

Then delete your branch, since it's done its job:

```bash
git branch -d feature/your-branch-name
git push origin --delete feature/your-branch-name
```

### 6. Test on dev

`dev` is the shared integration branch — this is where the team checks things
actually work together before they're trusted enough for `main`.

### 7. Promote dev to main

Once the team agrees `dev` is stable (say so in the team chat first):

```bash
git checkout main
git pull
git merge dev
git push
```

## Handling merge conflicts

A conflict means two people edited the same lines. If it happens (most likely while
merging your branch into `dev`, or `dev` into `main`):

1. Don't panic — nothing is lost.
2. Git marks the conflicting lines in the file with `<<<<<<<`, `=======`, `>>>>>>>`.
   Edit the file to keep the correct version, then delete those markers.
3. `git add <file>` and `git commit` to finish the merge.
4. If you're not sure what to keep, ask whoever wrote the other side before
   resolving it — don't guess on someone else's work.

## What not to commit

- Passwords, API keys, tokens — use a `.env` file and make sure it's listed in `.gitignore`.
- Build output / compiled files (e.g. `.class`, `target/`, `build/`) — these should
  be generated locally, not stored in Git.
- IDE-specific files (e.g. `.idea/`, `.vscode/` settings you haven't agreed on as a team).
- Large binaries (videos, big images) — if genuinely needed, ask the team first.

## Getting help

- Stuck on a Git error? Paste the exact error message in the team chat before trying
  random fixes — most Git problems are easy to solve once someone sees the actual message.
- GitHub's own docs are good: https://docs.github.com/get-started
