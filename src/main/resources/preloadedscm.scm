(define nil '())

;map
;supports single list only
(define (map proc list)
  (if (null? list) nil
      (cons (proc (car list)) (map proc (cdr list)))))

;for-each
(define (for-each proc list . lists)
  (if (not (null? list))
      (if (null? lists)
          (begin (proc (car list)) (for-each proc (cdr list)))
          (begin
            (apply proc (cons (car list)
                              (map (lambda (x) (car x)) lists)))
            (apply for-each
                   (cons proc
                         (cons (cdr list)
                               (map (lambda (x) (cdr x)) lists))))))))

;or, and
;ideally it should be implemented using macro, but anyway
(define (or x . y)
  (if x #t
      (if (null? y) #f
          (apply or y))))
(define (and x . y)
  (if (not x) #f
      (if (null? y) #t
          (apply and y))))

;=
(define (= x y)
  (if (and (number? x) (number? y))
      (eqv? x y)
      (error "arguments must be numbers.")))

