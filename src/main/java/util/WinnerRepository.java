package util;

public class WinnerRepository extends GsonRepository<MatchResults> {

    public WinnerRepository() {
        super(MatchResults.class);
    }

}