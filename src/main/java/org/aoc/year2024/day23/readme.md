
Bron Kerbosch algorithm is used to find all maximal cliques in an undirected graph. The algorithm is based on the principle of backtracking. The algorithm is named after two Dutch computer scientists, Joep Kerbosch and Piet Bron.

# Day 23: Network of Computers
```java
    public Set<String> findLargestParty(Set<String> setComputers, Map<String, Set<String>> mapConnections) {
        Set<String> largestClique = new HashSet<>();
        bronKerbosch(new HashSet<>(), new HashSet<>(setComputers), new HashSet<>(), mapConnections, largestClique);
        return largestClique;
    }

    private void bronKerbosch(Set<String> R, Set<String> P, Set<String> X,
                              Map<String, Set<String>> mapConnections, Set<String> largestClique) {
        if (P.isEmpty() && X.isEmpty()) {
            // Found a maximal clique
            if (R.size() > largestClique.size()) {
                largestClique.clear();
                largestClique.addAll(R);
            }
            return;
        }

        // Choose a pivot to reduce the number of recursive calls
        String pivot = P.stream().findFirst().orElse(null);
        Set<String> neighbors = pivot != null ? mapConnections.getOrDefault(pivot, new HashSet<>()) : new HashSet<>();

        // P \ neighbors
        Set<String> candidates = new HashSet<>(P);
        candidates.removeAll(neighbors);

        for (String v : candidates) {
            Set<String> newR = new HashSet<>(R);
            newR.add(v);

            Set<String> newP = new HashSet<>(P);
            newP.retainAll(mapConnections.getOrDefault(v, new HashSet<>()));

            Set<String> newX = new HashSet<>(X);
            newX.retainAll(mapConnections.getOrDefault(v, new HashSet<>()));

            bronKerbosch(newR, newP, newX, mapConnections, largestClique);

            P.remove(v);
            X.add(v);
        }
    }
```