(define nil '())

;for-each
;supports single list only
(define (for-each proc list)
  (if (not (null? list))
      (begin (proc (car list)) (for-each proc (cdr list)))))

;map
;supports single list only
(define (map proc list)
  (if (null? list) nil
      (cons (proc (car list)) (map proc (cdr list)))))