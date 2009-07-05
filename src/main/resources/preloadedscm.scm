(define nil '())

;for-each
;supports single list only
(define (for-each proc list)
  (if (not (null? list))
      (begin (proc (car list)) (for-each (cdr list)))))

;map
;supports single list only
(define (map proc list)
  (define (iter result list)
    (cond
     ((null? list) result)
     (else (iter (cons (proc (car list)) result)
                 (cdr list)))))
  (iter nil list))