This repository contains the code of the data abstractions developed in the context of the MSc. Thesis "Secure Abstractions for Trusted Cloud Computation" at Faculdade de Ciências e Tecnologias, Universidade Nova de Lisboa. The goal of this thesis was to provide a set of secure abstractions to serve as a tool for programmers to develop their own distributed applications. 

### Abstractions
We developed the following secure abstractions which can be found in this repository:
* Sets
* Lists
* Maps
* Register
* Counter

These secure abstractions were built based upon CRDTs and cryptographic algorithms. The code for the secure abstractions, which we named SCRDTs, CRDTs and cryptographic algorithms can be found at `src\crdts`.

### Project Structure
The source code for the structures is found in the `src` folder. The structure of this folder is as follows:

    .
    ├── benchmarks                 # Performance micro-benchamrks of the abstractions
    ├── client                     # Stubs source files
    ├── crdts                      # Secure and non secure abstractions source files
    ├── helpers                    # Helper classes
    └── tests                      # Unitary tests
    
### Pre-requisites
Java 8 or later must be installed.
