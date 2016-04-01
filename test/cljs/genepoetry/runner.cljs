(ns genepoetry.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [genepoetry.core-test]))

(doo-tests 'genepoetry.core-test)
