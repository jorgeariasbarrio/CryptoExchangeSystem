package com.order.portfolio_client.repository.impl;

import com.order.portfolio_client.model.Portfolio;
import com.order.portfolio_client.repository.PortfolioRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class PortfolioRepositoryCustomImpl implements PortfolioRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean reserveBalance(Long userId, Double balanceToReserve) {
        Query query = new Query(Criteria.where("userId").is(userId));

        Update update = new Update().inc("reservedBalance", balanceToReserve)
                .inc("balance", -balanceToReserve);
        query.addCriteria(Criteria.where("balance").gte(balanceToReserve));

        var result = mongoTemplate.updateFirst(query, update, Portfolio.class);

        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean reserveAsset(Long userId, Double quantity, String assetType) {

        Query query = new Query(Criteria.where("userId").is(userId));

        Update update = new Update().inc("reservedAsset." + assetType, quantity)
                .inc("asset." + assetType, -quantity);
        query.addCriteria(Criteria.where("asset." + assetType).gte(quantity));

        var result = mongoTemplate.updateFirst(query, update, Portfolio.class);

        return result.getModifiedCount() > 0;
    }
}
