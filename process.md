# Process

How we build this project. Every piece of work follows the same loop, and the
loop is designed around one goal: **David learns the concepts, not just the
code.** Documents come first, code comes last.

## The loop

```
1. Feature description        →  features/<feature-name>.md
2. Architecture definition    →  architecture/*.md (new or updated)
3. Architecture analysis      →  knowledge/*.md (topics updated)
4. Implementation             →  code, written from the documents above
5. Knowledge capture          →  knowledge/*.md (what David learned)
```

### Step 1 — Feature description (`features/`)

Before any code, write a markdown file in `features/` describing the feature in
plain language: what the user can do, what they see, what the rules are. No
technology choices here — a feature description should make sense to someone
who has never heard of Spring or React. Use `features/_template.md` as the
starting shape.

### Step 2 — Architecture definition (`architecture/`)

Decide *how* the feature will be built and record it in `architecture/`. This
means: which parts of the system are involved (database, backend, frontend),
what new tables/endpoints/pages are needed, and how data flows between them.
Update the existing architecture documents rather than letting them go stale —
they describe the system as it **is**, plus the agreed plan for what is being
built next.

### Step 3 — Architecture analysis → knowledge (`knowledge/`)

Read the architecture for the feature and ask: **what does David need to
understand to build this?** Every concept that shows up (a JOIN, a REST
endpoint, a React hook, a Gradle task...) maps to a topic file in `knowledge/`.
For each one:

- If the topic file says David already knows it → nothing to do.
- If it's new or only partly known → it becomes a learning goal for this
  feature. Add it to the topic file's "Next things to learn" before starting.

This is how we use the knowledge directory to **model what David knows**: it
is the single source of truth for his current understanding, and the gap
between it and the architecture is the lesson plan for the feature.

### Step 4 — Implementation

Write the code from the feature + architecture documents. While working,
explain every new concept as it appears (defining terms — see `CLAUDE.md`),
at a pace that matches the knowledge files: don't re-explain what David has
already mastered, do stop and teach what's marked as new.

### Step 5 — Knowledge capture

After the feature works, update the `knowledge/` topic files: move concepts
David now understands into "What David knows" (with a short note in his
reach — these notes are *for David to reference later*, so they should make
sense on their own months from now). If something stayed confusing, leave it
in "Next things to learn" with a note, and consider asking Scott (the
teacher) to walk through it.

## Directory map

| Location        | Contents                                                       |
| --------------- | -------------------------------------------------------------- |
| `features/`     | One markdown file per feature; written **before** the code.    |
| `architecture/` | How the system is built; kept current as features land.        |
| `knowledge/`    | One markdown file per subject; models what David knows.        |
| `process.md`    | This file — the loop above.                                    |
