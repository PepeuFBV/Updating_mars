# Mars Software Modification for MIPS Architecture - Updating Mars

## Project Description

This project is a modification of the Mars software for the MIPS architecture. The implementation corresponds to part of the operating system infrastructure to perform process management, such as creation of Syscalls, PCB, and process scheduling.

## Getting Started

These instructions will provide a copy of the project running on your local machine for development and testing purposes.

### Prerequisites

What you need to install the software:

- Java Runtime Environment (JRE) 8 or higher
- Mars MIPS Simulator

### Installation

1. Clone the repository
2. Navigate to the project folder
3. Compile the project

## Running the tests

To run the tests, you need to open the Mars MIPS Simulator and load the project file. Then, you can run created Syscalls etc etc --TODO

## Project Details

The project was developed under the Operating Systems course at the Federal University of the Semi-Arid Rural Area (UFERSA). The project was developed by the following students:
- Afonso Simão
- Breno Klywer
- João Emanoel
- Pedro Figueira
- Samuel Rogenes
- Vinícius Gabriel


## Implementation Steps

The project was divided into the following parts:

- Part 1
    - Step 1: PCB Creation, Process Table and Process Scheduling
    - Step 2: Syscalls Implementation for Process Management
    - Step 3.1: Preemptive Scheduling
    - Step 3.2: New Scheduling Algorithms
    - Step 4: Process Synchronization through Semaphores
- Part 2
    - Step 1: Virtual Memory Management
    - Step 2: Page Table Implementation

---

### Step 1: PCB Creation, Process Table and Process Scheduling

#### Process Control Block (PCB) Structure and Implementation

The first step of the project was to create the PCB structure and implement the process scheduling. The PCB structure was created with the following fields:

- A global pointer register
- An stack pointer register
- All general-purpose registers
- Program counter register
- Current process state
- Process ID
- Program address

The Process Control Block (PCB) Structure serves to store the process information, such as the process state, process ID, and the registers' values. It also has 2 important methods:

- `copyFromHardware()` - copies the values of the registers from the hardware to the PCB class
- `copyToHardware()` - copies the values of the registers from the PCB class to the hardware

#### Process Table Implementation

The process table was implemented as a Deque, for the ready processes (`Deque<ProcessControlBlock>`), and a variable for the currently in execution process. The process table has the methods for adding a process to the ready queue, removing a process from the ready queue and changing the state of a process. It also has a static counter for the process ID for newly created processes.

#### Process Scheduling Implementation

The process scheduling was implemented using a FIFO queue. The scheduling algorithm is simple: the process that is at the front of the queue is the one that will be executed. The scheduling algorithm only has the `shedule()` method, which is responsible for changing the next process state to `RUNNING` and copying the process information to the hardware.

---
### Step 2: Syscalls Implementation for Process Management

#### Syscalls Implementation

The Syscalls were implemented to allow the user to create a new process, change the process in execution and finish a process. So, the Syscalls implemented were:

- `SyscallFork`: Creates a new process by copying the current process information to a new PCB. The new process is added to the process table and the process ID is incremented. The parameters are the syscall number and the initial address of the new process.
- `SyscallProcessChange`: Changes the process in execution. The current process is added to the ready queue in the process table and the next process is removed from the ready queue and its state is changed to `RUNNING`. There is only one parameter, the syscall number.
- `SyscallProcessTerminate`: Terminates the process in execution. The current process is finished, its removed from the process table and the next process starts to execute. There is only one parameter, the syscall number.

Extra - For testing purposes the following assembly code was used:

```assembly
.include "macros.asm"

.data
.text             
	#criação dos processos
SyscallFork(Programa1)
	SyscallFork(Programa2)
	SyscallFork(Idle)
	#escalonando o primeiro processo
SyscallProcessChange
	
Idle:					
	loop:
SyscallProcessChange
		j loop
```

```assembly
Programa1:					
		addi $s1, $zero, 1 # valor inicial do contador
		addi $s2, $zero, 10 # valor limite do contador
	loop1: 	addi $s1, $s1, 1
		beq $s1, $s2, fim1
SyscallProcessChange
		j loop1
	fim1:	SyscallProcessTerminate

Programa2: 
		addi $s1, $zero, -1 # valor inicial do contador
		addi $s2, $zero, -10 # valor limite do contador
	loop2: 	addi $s1, $s1, -1
		beq $s1, $s2, fim2
SyscallProcessChange
		j loop2
	fim2:	SyscallProcessTerminate

```

---
### Step 3.1: Preemptive Scheduling

For the preemptive scheduling, the project was modified by the creation of a timer tool. The timer tool was implemented to interrupt the process in execution after a certain time. The user is able to set the time for the timer tool through inputing the number of instructions that the process in execution will execute before being interrupted, there are also 3 more options: Connect to MIPS, reset, help and exit. When the process is interrupted, the timer is reset and starts counting again.

The interruption switches the process in execution to the ready queue and the next process starts to execute. This mechanism is a substitute for `SyscallProcessChange` and is used to implement the preemptive scheduling.

Extra - For testing purposes the following assembly code was used:

```assembly	
.include "macros.asm"

.data
.text             
	#criação dos processos
SyscallFork(Programa1)
	SyscallFork(Programa2)
	SyscallFork(Idle)
	#escalonando o primeiro processo
SyscallProcessChange
	
Idle:					
	loop:
NOP
j loop
```

```assembly
Programa1:					
		addi $s1, $zero, 1 # valor inicial do contador
		addi $s2, $zero, 10 # valor limite do contador
	loop1: 	addi $s1, $s1, 1
		beq $s1, $s2, fim1
j loop1
	fim1:	SyscallProcessTerminate

Programa2: 
		addi $s1, $zero, -1 # valor inicial do contador
		addi $s2, $zero, -10 # valor limite do contador
	loop2: 	addi $s1, $s1, -1
		beq $s1, $s2, fim2
		j loop2
	fim2:	SyscallProcessTerminate
```

---
### Step 3.2: New Scheduling Algorithms

For the new scheduling algorithms, the project was modified by the creation of two new scheduling algorithms: Fixed Priority and Lottery. The Fixed Priority algorithm is a preemptive scheduling algorithm that assigns a priority to each process and the process with the highest priority is the one that will be executed. The Lottery algorithm is a non-preemptive scheduling algorithm that assigns a number of tickets to each process and the process that wins the lottery is the one that will be executed (in our implementation, the process is chosen randomly).

In the Timer Tool, the user is able to choose the scheduling algorithm that will be used.

Extra - For testing purposes the following assembly code was used:

```assembly
.include "macros.asm"

.data
.text             
      #criação dos processos com prioridade
SyscallFork(Programa1, 1)
	SyscallFork(Programa2, 2)
	SyscallFork(Idle, 0)
	#escalonando o primeiro processo
SyscallProcessChange
	
Idle:					
	loop:
NOP
j loop
```

```assembly
Programa1:					
		addi $s1, $zero, 1 # valor inicial do contador
		addi $s2, $zero, 10 # valor limite do contador
	loop1: 	addi $s1, $s1, 1
		beq $s1, $s2, fim1
j loop1
	fim1:	SyscallProcessTerminate

Programa2: 
		addi $s1, $zero, -1 # valor inicial do contador
		addi $s2, $zero, -10 # valor limite do contador
	loop2: 	addi $s1, $s1, -1
		beq $s1, $s2, fim2
		j loop2
	fim2:	SyscallProcessTerminate
```

---
### Step 4: Process Synchronization through Semaphores