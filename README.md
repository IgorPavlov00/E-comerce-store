# Sole&Seam

A sneaker and denim store built with Angular, Spring Boot, PostgreSQL and Stripe Checkout.

Spring Boot serves both the API (under `/api`) and the built Angular app, so the whole store deploys as one service.

## Features

- Catalog with search, category, brand, colour and price filters, plus sorting
- Cart saved in the browser, with free shipping from $150
- Stripe Checkout for guests and signed-in customers. Prices are always read from the database, never from the browser.
- Accounts with hashed passwords, token sign-in and order history
- Optional email confirmation and order receipts

## Run it locally

You need Java 17, Node 18 and PostgreSQL. Create a database called `Ecomerce`; tables and products are created on first start.

Backend, on port 8084. The variables are listed in [.env.example](.env.example):

```bash
cd ecomerce
DB_PASSWORD=your-password STRIPE_SECRET_KEY=sk_test_... ./mvnw spring-boot:run
```

Frontend, on port 4200, which forwards `/api` to the backend:

```bash
cd front
npm ci
npm start
```

To pay in test mode, use card `4242 4242 4242 4242` with any future date and any CVC.

## Tests

```bash
cd ecomerce && ./mvnw test   # uses an in-memory database
cd front && npm test         # needs Chrome
```

## Deploy to Render

[render.yaml](render.yaml) defines the web service and its database.

1. In Render, choose **New > Blueprint** and connect this repository.
2. Enter your Stripe test secret key when asked for `STRIPE_SECRET_KEY`.
3. Apply. The first build takes several minutes.

Limits of Render's free plan:

- The service sleeps after 15 minutes without traffic, and the next visit takes about a minute to wake it.
- The free database **expires 30 days after creation** and is deleted 14 days later unless upgraded. A workspace can have only one.
- Outbound SMTP is blocked, so email goes through Brevo's HTTPS API instead.

## Email confirmation

Without email set up, new accounts are active immediately. To turn on confirmation emails and order receipts:

1. Create a free [Brevo](https://www.brevo.com) account and verify your sender address under **Senders**.
2. Create an API key under **SMTP & API > API Keys**.
3. Set `BREVO_API_KEY` and `MAIL_FROM` (the verified sender) on the service.

New accounts must then confirm their email before signing in. Existing accounts that never confirmed get a fresh link when they next sign in.

## Before taking real payments

The site footer and cart describe the store as a test-mode demo. Update that text in `front/src/app/layout/footer.component.ts` and `front/src/app/pages/cart.component.ts` when you switch to a live Stripe key.

Orders are marked paid when the customer returns from Stripe. A customer who closes the tab before that stays `PENDING`, so add a Stripe webhook before relying on this for real orders.
