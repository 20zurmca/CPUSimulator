.wordsize 16              ; sets the machine wordsize
.regcnt    4              ; 4 general purpose registers
.maxmem   0x1000          ; max memory size is 16 bytes

.pos 0x0
main:  MOVEZ x0, data     ; store pointer at data to x0
	   LDUR x1, [x0, #0]  ; load data at start into register x1
	   CBZ  x2, #32       ; Branch to memory location 32 if x2 is 0
	   ADDI x2, x2, #20   ; add 20 to x2
       SUB x2, x2, x1    ;  SUB 0xAB and 20 and store in register x2
	   PUSH x2            ; push x2 onto the stack 
	   NOP                ; Get the next instruction
	   POP  x3            ; pop x2 from stack, store data into x3
	   STUR x3, [x0, #0]  ; store x3 into data (over write)
	   EOR  x3, x3, x2    ; perform bitwise xor on x2 and x3 (should be 0 since they are the same)
	   CBZ x3, #4         ; Branch to the halt if x3 is 0 (second pass)
       B    #12           ; Branch back to ADDI
       HALT               ; halt the processor
	   

.pos 0x100                ; set image location to 0x100
.align 8                  ; align data to an 8-byte boundry

data:   
        .half   0x0AB     ; place 0xAB in a 2-byte location
        .byte   0x0AB  	  ; place 0xAB in a 1-byte location
        .double 0x0AB     ; place 0xAB in a 8-byte location
        .single 0x0AB     ; place 0xAB in a 4-byte location
        
 
.pos 0x200                ; set the image location to 0x200
stack:  .double 0xDEF     ; start the stack here and create an 8-byte data value