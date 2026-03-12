# 🛍️ ECommerceOne

A clean, lightweight Android e-commerce app featuring product browsing, real-time search, and a shopping cart — built with Java and SQLite.

---

## 📱 Screenshots

> _Add your screenshots here_

---

## ✨ Features

- **Product Grid** — Browse products in a 2-column grid layout
- **Live Search** — Filter products in real-time as you type
- **Shopping Cart** — Add/remove products with a persistent cart
- **Cart Badge** — Floating action button shows live item count
- **SQLite Storage** — All data stored locally using `DatabaseHelper`

---

## 🏗️ Project Structure

```
com.example.ecommerceone/
├── MainActivity.java         # Product listing, search, FAB cart badge
├── CartActivity.java         # Cart screen
├── ProductAdapter.java       # RecyclerView adapter with cart callbacks
├── DatabaseHelper.java       # SQLite CRUD operations
└── Product.java              # Product model
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| UI | XML Layouts + Material Design |
| Database | SQLite via `SQLiteOpenHelper` |
| Lists | `RecyclerView` + `GridLayoutManager` |
| Navigation | `FloatingActionButton` + `Intent` |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (Hedgehog or later)
- Android SDK 21+
- Gradle 8+

### Installation

1. **Clone the repo**
   ```bash
   git clone https://github.com/YOUR_USERNAME/ECommerceOne.git
   ```

2. **Open in Android Studio**
   - File → Open → select the project folder

3. **Run the app**
   - Connect a device or start an emulator
   - Click ▶️ Run

---

## 📖 How It Works

### Product Listing
`MainActivity` loads all products from SQLite on startup and displays them in a 2-column `RecyclerView` grid.

### Search
A `TextWatcher` on the search `EditText` queries the database on every keystroke and refreshes the adapter with filtered results.

### Cart Badge
The FAB cart button shows a live count badge (`tvCartCount`). The count updates whenever:
- A product is added/removed via `onCartCountChanged()` callback
- The activity resumes (`onResume`)

### Cart Screen
Tapping the FAB launches `CartActivity` where users can review and manage their cart items.

---

## 🗄️ Database Schema

### Products Table
| Column | Type | Description |
|---|---|---|
| id | INTEGER | Primary key |
| name | TEXT | Product name |
| price | REAL | Product price |
| image | TEXT/BLOB | Product image |

### Cart Table
| Column | Type | Description |
|---|---|---|
| id | INTEGER | Primary key |
| product_id | INTEGER | Foreign key → products |
| quantity | INTEGER | Item quantity |

---

## 🔮 Roadmap

- [ ] Product detail screen
- [ ] Checkout flow
- [ ] Categories/filter
- [ ] Order history
- [ ] Cloud sync (Firebase)

---

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 👤 Author

**Your Name**
- GitHub: [@AnonymousV73X](https://github.com/AnonymousV73X/)

---

> Built as part of INTE 413 — Mobile Applications Programming, Kabarak University
