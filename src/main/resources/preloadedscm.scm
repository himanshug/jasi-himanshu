(define nil '())

;map
;supports single list only
(define (map-single-list proc list)
  (if (null? list) nil
      (cons (proc (car list)) (map-single-list proc (cdr list)))))
(define (map proc list . lists)
  (if (null? list)
      nil
      (if (null? lists)
          (map-single-list proc list)
          (cons (apply proc
                       (map-single-list (lambda (x) (car x))
                                        (cons list lists)))
                (apply map 
                       (cons proc
                             (cons (cdr list)
                                   (map-single-list (lambda (x) (cdr x)) lists))))))))

;for-each
(define (for-each proc list . lists)
  (if (not (null? list))
      (if (null? lists)
          (begin (proc (car list)) (for-each proc (cdr list)))
          (begin
            (apply proc (cons (car list)
                              (map-single-list (lambda (x) (car x)) lists)))
            (apply for-each
                   (cons proc
                         (cons (cdr list)
                               (map-single-list (lambda (x) (cdr x)) lists))))))))

;or, and
;ideally it should be implemented using macro, but anyway
(define (or-aux arg-list)
  (cond ((null? arg-list) #f)
        ((car arg-list) (car arg-list))
        (else (or-aux (cdr arg-list)))))
(define or
  (lambda args
    (if (null? args) #f
        (or-aux args))))
(define (and-aux arg-list)
  (cond ((null? (cdr arg-list)) (car arg-list))
        ((car arg-list) (and-aux (cdr arg-list)))
        (else #f)))
(define and
  (lambda args
    (if (null? args) #t
        (and-aux args))))


;all '=' related
(define (=aux x y type-pred eq-pred)
  (if (and (type-pred x) (type-pred y))
      (eq-pred x y)
      (error "arguments not of valid type")))
(define (char=? x y) (=aux x y char? eqv?))
(define (string=? x y) (=aux x y string? eqv?))

(define =
  (lambda args
    (define (iter args)
      (cond
       ((null? args) #t)
       ((null? (cdr args)) #t)
       ((=aux (car args) (cadr args) number? eqv?)
        (iter (cdr args)))
       (else #f)))
    (iter args)))

;pair related
(define (caar x)
  (car (car x)))
(define (cadr x)
  (car (cdr x)))
(define (cddr x)
  (cdr (cdr x)))

;list related
(define (ass-pred obj alist pred)
  (cond ((null? alist) #f)
        ((pred obj (caar alist)) (car alist))
        (else (ass-pred obj (cdr alist) pred))))
(define (assq obj alist)
  (ass-pred obj alist eq?))
(define (assv obj alist)
  (ass-pred obj alist eqv?))
(define (assoc obj alist)
  (ass-pred obj alist equal?))

(define (mem-pred obj list pred)
  (cond ((null? list) #f)
        ((pred obj (car list)) list)
        (else (mem-pred obj (cdr list) pred))))
(define (memq obj list) (mem-pred obj list eq?))
(define (memv obj list) (mem-pred obj list eqv?))
(define (member obj list) (mem-pred obj list equal?))

(define (length list)
  (define (length-aux list result)
    (if (null? list) result
        (length-aux (cdr list) (+ result 1))))
  (length-aux list 0))

(define (reverse list)
  (define (reverse-aux list result)
    (if (null? list) result
        (reverse-aux (cdr list) (cons (car list) result))))
  (reverse-aux list nil))

(define (append list1 obj)
  (define (append-aux list result)
    (if (null? list) result
        (append-aux (cdr list) (cons (car list) result))))
  (append-aux (reverse list1) obj))

(define (list-ref list i)
  (if (= i 0) (car list)
      (list-ref (cdr list) (- i 1))))

(define (list->string list)
  (define (iter list i result)
    (if (null? list) result
        (begin
          (string-set! result i (car list))
          (iter (cdr list) (+ i 1) result))))
  (iter list 0 (make-string (length list))))

;string related
(define string (lambda args
          (list->string args)))

;; numbers related
(define (zero? x) (= x 0))
(define (positive? x) (> x 0))
(define (negative? x) (< x 0))

(define (abs x)
  (if (positive? x) x (* -1 x)))
(define (quotient x y) (/ x y))
(define (remainder x y)
  (- x (* (/ x y) y)))
(define (modulo x y)
  (let ((r (remainder x y)))
    (if (negative? (* r y)) (+ r y) r)))

(define (max x . y)
  (if (null? y) x
      (begin
        (if (> x (car y))
            (set-car! y x))
        (apply max y))))
(define (min x . y)
  (if (null? y) x
      (begin
        (if (< x (car y))
            (set-car! y x))
        (apply min y))))
;todo
;even, odd, gcd, lcm


;; char related
(define (char>? c1 c2)
  (not (or (char<? c1 c2) (char=? c1 c2))))
(define (char<=? c1 c2)
  (or (char<? c1 c2) (char=? c1 c2)))
(define (char>=? c1 c2)
  (not (char<? c1 c2)))
(define (char-ci=? c1 c2)
  (char=? (char-downcase c1) (char-downcase c2)))
(define (char-ci<? c1 c2)
  (char<? (char-downcase c1) (char-downcase c2)))
(define (char-ci>? c1 c2)
  (char>? (char-downcase c1) (char-downcase c2)))
(define (char-ci<=? c1 c2)
  (char<=? (char-downcase c1) (char-downcase c2)))
(define (char-ci>=? c1 c2)
  (char>=? (char-downcase c1) (char-downcase c2)))
