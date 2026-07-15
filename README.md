# Tunstall Coding Compression Algorithm

## Overview

This project implements **Tunstall Coding**, a variable-to-fixed length lossless compression algorithm, in Java. The program generates a random source sequence based on predefined symbol probabilities, constructs a Tunstall dictionary, encodes and decodes the sequence, and compares its performance with Huffman coding.

The implementation also calculates several information theory metrics, including source entropy, average code length, compression efficiency, and compression ratio.

---

## Features

- Generates a random source sequence using a predefined probability distribution
- Builds a Tunstall dictionary automatically
- Encodes the source sequence into fixed-length binary codewords
- Decodes the encoded sequence back to the original message
- Verifies decoding correctness
- Calculates compression statistics
- Compares Tunstall coding with Huffman coding

---

## Probability Distribution

The source alphabet consists of four symbols:

| Symbol | Probability |
|--------|------------:|
|    d   |     0.70    |
|    c   |     0.15    |
|    b   |     0.10    |
|    a   |     0.05    |

A random source sequence of 1000 symbols is generated using this distribution.

---

---

## Project Structure

```
Implementation/
├── .idea/                          # IntelliJ project files
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── example/
│                   ├── Main.java   # Core algorithm: sequence generation, Tunstall dictionary, encode/decode, statistics
│                   └── Pair.java   # Helper class storing a phrase and its probability
├── .gitignore
└── pom.xml                         # Maven build configuration
```

---

## How the Algorithm Works

### Tunstall Coding

The program:

1. Generates a random source sequence.
2. Builds a Tunstall dictionary by repeatedly expanding the most probable phrase.
3. Assigns fixed-length binary codewords to each dictionary entry.
4. Encodes the generated sequence.
5. Decodes the encoded bitstream.
6. Verifies that the decoded message matches the original sequence.

---

## Huffman Comparison

The project also includes a comparison with Huffman coding using the same source probabilities.

The comparison reports:

- Average code length
- Total encoded message length
- Compression ratio compared to ASCII encoding
- Coding efficiency

---

## Output

The program displays:

- Generated source sequence
- Tunstall dictionary
- Binary codewords
- Encoded message
- Decoded message
- Verification of successful decoding
- Tunstall statistics
- Huffman statistics
- Compression efficiency

---

## Technologies Used

- Java
- Java Collections Framework
- BigDecimal
- HashMap
- ArrayList
- Random

---

## Information Theory Metrics

The implementation calculates:

- Average phrase length
- Average encoded length (bits/symbol)
- Source entropy
- Tunstall coding efficiency
- Huffman average code length
- Huffman compression ratio
- Huffman coding efficiency

---

## Learning Objectives

This project demonstrates:

- Variable-to-fixed length coding
- Dictionary construction
- Lossless data compression
- Source entropy calculation
- Coding efficiency analysis
- Comparison of Tunstall and Huffman coding

---

---

## How to Run

### Prerequisites
- Java 17+ (JDK)
- Maven

### Build
```bash
cd Implementation
mvn compile
```

### Run
```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Or, after packaging a jar:
```bash
mvn package
java -cp target/classes org.example.Main
```

> Note: check whether `pom.xml` includes the `exec-maven-plugin`. If it doesn't, either add it or just run `Main.java` directly from your IDE (IntelliJ/VS Code).

---

## Report

A detailed write-up of the theory, methodology, and results is available in [`tunstall_report.pdf`](./tunstall_report.pdf).

## Authors

**Simona Cholakova**
**Alja Eremić**
