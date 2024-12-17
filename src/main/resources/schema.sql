
-- Borrower Table: Represents library members who can borrow books
CREATE TABLE borrower (
    borrower_id BIGSERIAL PRIMARY KEY,    -- Unique identifier for each borrower
    name VARCHAR NOT NULL,                -- Name of the borrower
    email VARCHAR NOT NULL                -- Email address for the borrower
);

-- Book Table: Represents both catalog entries and physical copies of books
CREATE TABLE book (
    book_id BIGSERIAL PRIMARY KEY,        -- Unique identifier for each book
    isbn VARCHAR,                         -- ISBN number of the book (can be duplicate)
    title VARCHAR NOT NULL,               -- Title of the book
    author VARCHAR,                       -- Author of the book
    borrower_id BIGINT,                   -- References the borrower currently holding the book
    FOREIGN KEY (borrower_id) REFERENCES borrower (borrower_id) ON DELETE SET NULL
);
