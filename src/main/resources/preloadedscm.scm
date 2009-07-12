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

;and
;ideally it should be implemented using macro, but anyway
(define (and x . y)
  (if x #t
      (if (null? y) #f
          (apply and y))))

;=
(define (= x y)
  (if (and (number? x) (number? y))
      (eqv? x y)
      (error "arguments must be numbers.")))

