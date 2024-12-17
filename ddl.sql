-- Book Table: Represents catalog or template entries of books
CREATE TABLE book (
    book_id BIGSERIAL PRIMARY KEY, -- Unique identifier for each book template
    title VARCHAR NOT NULL,        -- Title of the book
    isbn VARCHAR UNIQUE NOT NULL,  -- ISBN number of the book (unique identifier for the template)
    summary TEXT                   -- Short description or summary of the book
);

-- Book Copy Table: Represents physical copies of books available in the library
CREATE TABLE book_copy (
    copy_id BIGSERIAL PRIMARY KEY, -- Unique identifier for each physical book copy
    book_id BIGINT NOT NULL,       -- References the book template this copy belongs to
    qr_code VARCHAR UNIQUE NOT NULL, -- Unique QR code to identify the physical copy
    FOREIGN KEY (book_id) REFERENCES book (book_id) ON DELETE CASCADE -- Ensures copies are deleted if the book is removed
);

-- Patron Table: Represents library members who can borrow books
CREATE TABLE patron (
    patron_id BIGSERIAL PRIMARY KEY, -- Unique identifier for each patron/member
    name VARCHAR NOT NULL,           -- Full name of the patron
    email VARCHAR UNIQUE NOT NULL    -- Unique email address for the patron
);

-- Borrow Table: Tracks borrowing transactions for physical book copies
CREATE TABLE borrow (
    borrow_id BIGSERIAL PRIMARY KEY, -- Unique identifier for each borrowing transaction
    copy_id BIGINT NOT NULL,         -- References the specific book copy being borrowed
    patron_id BIGINT NOT NULL,       -- References the patron borrowing the book
    borrow_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Date when the book was borrowed
    due_date TIMESTAMP NOT NULL,     -- Due date for returning the borrowed book
    FOREIGN KEY (copy_id) REFERENCES book_copy (copy_id) ON DELETE CASCADE, -- Ensures borrow records are removed if a book copy is deleted
    FOREIGN KEY (patron_id) REFERENCES patron (patron_id) ON DELETE CASCADE -- Ensures borrow records are removed if a patron is deleted
);
