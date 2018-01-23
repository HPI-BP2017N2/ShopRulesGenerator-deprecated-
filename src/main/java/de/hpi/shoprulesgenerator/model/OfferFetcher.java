package de.hpi.shoprulesgenerator.model;

import de.hpi.restclient.clients.BPBridgeClient;
import de.hpi.restclient.dto.GetRandomOffersResponse;
import de.hpi.restclient.pojo.Offer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class OfferFetcher implements Iterator<Offer>{

    private BPBridgeClient bpBridgeClient;
    private int fetchedPages = 0, pageSize;
    private Iterator<Offer> listIterator;
    private List<Offer> offers;
    private long shopID;

    public OfferFetcher(BPBridgeClient bpBridgeClient, long shopID, int pageSize) {
        setBpBridgeClient(bpBridgeClient);
        setShopID(shopID);
        setPageSize(pageSize);
        fetchNextPage();
    }

    @Override
    public boolean hasNext() {
        return getListIterator().hasNext();
    }

    @Override
    public Offer next() {
        return getListIterator().next();
    }

    @Override
    public void remove() {
        getListIterator().remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super Offer> action) {
        getListIterator().forEachRemaining(action);
    }

    //actions
    private void updateListIterator() {
        setListIterator(getOffers().iterator());
    }
    private void fetchNextPage() {
        GetRandomOffersResponse response = getBpBridgeClient().getRandomOffer(getShopID(), getPageSize(),
                calculateOffset());
        incrementFetchedPages();
        setOffers(response.getOffers());
        updateListIterator();
    }
    private void incrementFetchedPages() {
        setFetchedPages(getFetchedPages() + 1);
    }
    //computations
    private int calculateOffset() {
        return getPageSize() * getFetchedPages();
    }
}
