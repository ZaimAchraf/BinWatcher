package com.binwatcher.adminservice.controller;

import com.binwatcher.adminservice.model.Bin;
import com.binwatcher.adminservice.service.BinsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BinsController.class)
public class BinControllerTest {
    private final String BASE_URL = "/api/admin/bins";
    @MockitoBean
    private BinsService binsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGetAll() throws Exception {
        Bin bin1 = new Bin();
        Bin bin2 = new Bin();

        bin1.setLocation("test location 1");
        bin2.setLocation("test location 2");
        List<Bin> bins = Arrays.asList(bin1, bin2);

        when(binsService.getAll()).thenReturn(bins);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bins)));

        verify(binsService).getAll();
    }

    @Test
    public void testCreateBin() throws Exception {
        Bin bin = new Bin();
        bin.setLocation("Test location");

        when(binsService.create(bin)).thenReturn(bin);

        mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(bin)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(bin)));

        verify(binsService).create(any(Bin.class));
    }

    @Test
    public void testCreateBinFailedCase() throws Exception {
        Bin bin = new Bin();

        when(binsService.create(bin)).thenThrow(new IllegalArgumentException("invalid"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bin)))
                .andExpect(status().isBadRequest());

        verify(binsService).create(any(Bin.class));
    }

    @Test
    public void testUpdateBin() throws Exception {
        Bin bin = new Bin();
        bin.setLocation("test location");

        when(binsService.update("1", bin)).thenReturn(bin);

        mockMvc.perform(put(BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(bin)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bin)));

        verify(binsService).update(eq("1"), any(Bin.class));
    }

    @Test
    public void testUpdateBinFailedCase() throws Exception {
        Bin bin = new Bin();
        bin.setLocation("test location");

        when(binsService.update("2", bin)).thenThrow(new IllegalArgumentException("Invalid"));

        mockMvc.perform(put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bin)))
                .andExpect(status().isBadRequest());

        verify(binsService).update(eq("2"), any(Bin.class));
    }

    @Test
    void shouldUpdateFillLevel_whenPatchCalled() throws Exception {
        Bin bin = new Bin();
        bin.setFillLevel(90);
        when(binsService.updateFillLevel("1", 90)).thenReturn(bin);

        mockMvc.perform(patch(BASE_URL + "/1/fill-level?level=90"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bin)));

        verify(binsService).updateFillLevel("1", 90);
    }

    @Test
    void shouldDeleteBin_whenDeleteCalled() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/4"))
                .andExpect(status().isNoContent());

        verify(binsService).delete("4");
    }
}
