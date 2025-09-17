---
title: Slide 3 — API & Backend (Segmented for Stakeholders)
markmap:
  initialExpandLevel: 1
  colorFreezeLevel: 1
  maxWidth: 240
---

# API & Backend (Abraham)
## What I'll cover
- API design
    - Why it matters: Clear, predictable routes make integration easy and reduce bugs.
- Auth & roles
    - Why it matters: Protects data and lets us control who can do what.
- Data flow
    - Why it matters: Clean layers make changes safer and faster.
- Deployment
    - Why it matters: Same image in dev/prod means fewer surprises.
- Security / Scale / Performance
    - Why it matters: Keeps the app safe, fast, and ready to grow.

## API design (examples)
- /api/auth → register, login, me
- /api/users → profile, password
- /api/usage → daily, cumulative, summary
- /api/generation → summaries, time-series
- /api/rates → price plans

## Auth & roles
- JWT on login (Bearer header)
    - Why it matters: Stateless security works well behind a load balancer.
- Spring Security checks token on every call
    - Why it matters: Requests are verified before they touch business logic.
- Roles: USER, ADMIN, STAFF
    - Why it matters: Limits access to sensitive actions.

## Data flow
- Controller → Service → Repository → MySQL
    - Why it matters: Each layer does one job; easier to test and maintain.
- Services shape aggregates for charts
    - Why it matters: Frontend gets exactly the data it needs.

## Deployment
- Spring Boot container
- Config from env/secret files
- Managed ingress (Render-style)
    - Why it matters: Quick rollouts and simple rollback.

## Security / Scale / Performance
- BCrypt passwords, token expiry, CORS allow-list
    - Why it matters: Protects accounts and blocks unwanted origins.
- Stateless scaling; DB indexed for frequent queries
    - Why it matters: Add API instances without session stickiness.
- Watch p95 latency; plan caching for heavy reads
    - Why it matters: We track user impact and tune where it counts.